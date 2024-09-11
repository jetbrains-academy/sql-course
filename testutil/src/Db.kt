import org.sqlite.SQLiteDataSource
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.PreparedStatement
import java.sql.SQLException

class Db(dbName: String, inMemory: Boolean = false) {
    private val dataSource = SQLiteDataSource().also {
        it.url = "jdbc:sqlite:${if (inMemory) ":memory:" else "$dbName.sqlite"}"
        try {
            it.connection.use { cxn ->
                cxn.autoCommit = true
            }
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    fun executeFile(resourceName: String): List<Map<String, String?>> {
        val input = Files.newInputStream(Paths.get(resourceName)) ?: return emptyList()
        val results = mutableListOf<Map<String, String?>>()
        val rawSql = input.reader().use { it.readLines() }
        val filteredSql = rawSql.filter { !it.trim().startsWith("--") }
            .joinToString("\n")

        val sqlStatements = filteredSql.splitToSequence(";")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        sqlStatements.forEach { statement ->
            results.addAll(execute(statement.trim()))
        }

        return results
    }

    fun forEachQueryInFile(resourceName: String, code: (Db, Int, String) -> Unit) {
        val input = javaClass.getResourceAsStream(resourceName)?.reader()?.readLines()?.toMutableList() ?: return
        // trailing comment will process the last query like all the previous queries in the loop below
        input.add("--")

        val queries = mutableListOf<String>()
        val currentQuery = StringBuilder()
        var isComment = false
        input.map { it.trim() }.forEach {
            if (it.startsWith("--")) {
                if (!isComment) {
                    isComment = true
                    if (currentQuery.isNotBlank()) {
                        queries.add(currentQuery.toString())
                    }
                    currentQuery.clear()
                }
            } else {
                isComment = false
                currentQuery.append(it)
            }
        }

        queries.forEachIndexed { idx, query -> code(this, idx, query) }
    }

    private fun stringsToPairsConverter(expected: List<String>): List<Map<String, String>> {
        val columnNames = expected.first().split("|").map { it.trim() }
        val mappedValues = expected.drop(1).map { line ->
            val values = line.split("|").map { it.trim() }
            columnNames.zip(values).toMap()
        }
        return mappedValues
    }

    private fun showAsTable(data: List<Map<String, String?>>): String{
        if (data.isEmpty()) {
            return ""
        }
        var result = ""
        val headers = data.first().keys
        val rows = data.map { row -> headers.map { header -> row[header] ?: "<null>" } }
        // Calculate the maximum width for each column
        val columnWidths = headers.mapIndexed { index, header ->
            maxOf(header.length, rows.maxOf { it[index].length })
        }
        // Create a format string for each row
        val formatString = columnWidths.joinToString(" | ", prefix = "| ", postfix = " |") { "%-${it}s" }

        // Print headers
        result += formatString.format(*headers.toTypedArray()) + "\n"
        result += columnWidths.joinToString("-|-", prefix = "|-", postfix = "-|") { "-".repeat(it) }  + "\n"

        // Print rows
        rows.forEach { row ->
            result += formatString.format(*row.toTypedArray()) + "\n"
        }

        return result
    }

    private fun generateErrorMessage(message: String, query: String, expected: List<Map<String, String?>>, actual: List<Map<String, String?>>): String{
        return message + " \n\n" +
                "EXPECTED: \n" +
                showAsTable(expected) + "\n" +
                "ACTUAL: \n"+
                showAsTable(actual) + "\n" +
                "QUERY: \n" +
                query + "\n\n"
    }
    
    fun executeAndEvaluate(sqlStatement: String, expected: List<String>, placeholders: List<Any> = mutableListOf()): EvaluationResult {
        val expectedPairs = stringsToPairsConverter(expected)
        val actual = executeWithParameters(sqlStatement, placeholders)
        if(actual.isEmpty()) {
                return EvaluationResult(generateErrorMessage("Query returned an empty result", sqlStatement, expectedPairs, actual),
                    "not empty result", "empty result")
        }

        if (expectedPairs.size != actual.size) {
            return EvaluationResult(generateErrorMessage("The query returned an incorrect number of rows", sqlStatement, expectedPairs, actual),
                expectedPairs.size.toString(), actual.size.toString())
        }
        for (i in expectedPairs.indices) {
            val actualRow = actual[i]
            val expectedRow = expectedPairs[i]
            if (actualRow.size != expectedRow.size) {
                return EvaluationResult(generateErrorMessage("The query returned an incorrect number of columns", sqlStatement, expectedPairs, actual)
                    , expectedRow.size.toString(), actualRow.size.toString())
            }
            expectedRow.forEach { (key, expectedValue) ->
                if(!actualRow.containsKey(key)){
                    return EvaluationResult(generateErrorMessage("Column '$key' not found", sqlStatement, expectedPairs, actual),
                        "column exists", "column missed")
                }
                val actualValue = actualRow[key] ?: "<null>"
                    if (expectedValue != actualValue) {
                        var message = "The value in '$key' column is wrong."
                        for(ph in placeholders){
                            message += " With ? = '$ph';"
                        }
                        return EvaluationResult(generateErrorMessage(message, sqlStatement, expectedPairs, actual),
                            expectedValue, actualValue)
                    }
            }
        }
        return EvaluationResult("Ok", null, null)
    }


    private fun execute(sqlStatement: String): List<Map<String, String?>> {
        return executeWithParameters(sqlStatement, emptyList())
    }

    private fun executeWithParameters(sqlStatement: String, parameters: List<Any>): List<Map<String, String?>> {
        try {
            dataSource.connection.use { cxn ->
                // Determine if the statement should return results or not
                val isQuery = sqlStatement.trim().startsWith("SELECT", ignoreCase = true) ||
                        sqlStatement.trim().startsWith("PRAGMA", ignoreCase = true)
                val rows = mutableListOf<Map<String, String?>>()

                cxn.prepareStatement(sqlStatement).use { ps ->
                    setParameters(ps, parameters)
                    if (isQuery) {
                        // Execute and handle the query result set
                        ps.executeQuery().use { rs ->
                            while (rs.next()) {
                                val values = mutableMapOf<String, String?>()
                                for (i in 1..rs.metaData.columnCount) {
                                    values[rs.metaData.getColumnName(i).lowercase()] = rs.getString(i)
                                }
                                rows.add(values)
                            }
                        }
                    } else {
                        // Execute and handle non-query statements
                        ps.executeUpdate()
                    }
                }

                return rows
            }
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    private fun setParameters(ps: PreparedStatement, parameters: List<Any>) {
        parameters.forEachIndexed { index, parameter ->
            ps.setObject(index + 1, parameter)
        }
    }
}
