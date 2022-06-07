import org.h2.jdbcx.JdbcDataSource
import java.sql.SQLException
import java.text.MessageFormat

class Db(dbName: String) {
    private val dataSource = JdbcDataSource().also {
        it.setURL("jdbc:h2:mem:$dbName;DB_CLOSE_DELAY=-1")
        try {
            it.connection.use { cxn ->
                cxn.autoCommit = true
                cxn.createStatement().execute("CREATE TABLE Planet(id INT, name VARCHAR)")
                cxn.createStatement().execute("INSERT INTO Planet(id, name) VALUES (1, 'The Earth')")
            }
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    fun executeFromFile(resourceName: String): List<Map<String, String?>> {
        val input = javaClass.getResourceAsStream(resourceName) ?: return emptyList()
        return input.reader().readLines().firstOrNull { !it.trim().startsWith("--") }?.let {
            execute(it)
        } ?: emptyList()
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

    fun executeAndScore(sqlStatement: String, expected: Map<String, String>, placeholders: List<Any> = mutableListOf()): ScoredSolution {
        val query = if (placeholders.isEmpty()) sqlStatement else MessageFormat.format(sqlStatement.replace("'", "''"), *placeholders.toTypedArray())
        println(query)
        execute(query).let { resultSet ->
            val assessment = if (resultSet.isEmpty()) {
                "Query returned an empty result" to 0.0
            } else {
                val row = resultSet[0]
                val scores = expected.map { (key, value) ->
                    row[key]?.let {
                        if (value != it) {
                            "The value in 'result' column is $it. Expected: $value" to 0.0
                        } else {
                            "OK" to 1.0
                        }
                    } ?: run {
                        "Column 'result' not found" to 0.0
                    }
                }
                scores.firstOrNull { it.second == 0.0 } ?: "OK" to 1.0
            }
            return ScoredSolution(query, assessment.first, assessment.second)
        }
    }
    fun execute(sqlStatement: String): List<Map<String, String?>> {
        try {
            dataSource.connection.use { cxn ->
                val rows = mutableListOf<Map<String, String?>>()
                cxn.createStatement().executeQuery(sqlStatement).use { rs ->
                    while (rs.next()) {
                        val values = mutableMapOf<String, String>()
                        for (i in 1..rs.metaData.columnCount) {
                            values[rs.metaData.getColumnName(i).lowercase()] = rs.getString(i)
                        }
                        rows.add(values)
                    }
                }
                return rows
            }
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }
}