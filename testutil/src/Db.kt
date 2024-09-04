import org.h2.jdbcx.JdbcDataSource
import java.sql.PreparedStatement
import java.sql.SQLException

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

    fun executeAndEvaluate(sqlStatement: String, expected: Map<String, String>, placeholders: List<Any> = mutableListOf()): EvaluationResult {
        executeWithParameters(sqlStatement, placeholders).let { resultSet ->
            val result = if (resultSet.isEmpty()) {
                EvaluationResult("Query returned an empty result", "not empty result", "empty result")
            } else {
                val row = resultSet[0]
                val scores = expected.map { (key, value) ->
                    row[key]?.let {
                        if (value != it) {
                            var message = "The value in '$key' column is wrong."
                            for(ph in placeholders){
                                message += " With ? = '$ph';"
                            }
                            EvaluationResult(message, value, it)
                        } else {
                            EvaluationResult("Ok", value, it)
                        }
                    } ?: run {
                        EvaluationResult("Column '$key' not found", "column exists", "column missed")
                    }
                }
                scores.firstOrNull { it.actual != it.expected } ?: EvaluationResult("Ok", null, null)
            }
            return result
        }
    }

    private fun execute(sqlStatement: String): List<Map<String, String?>> {
        return executeWithParameters(sqlStatement, emptyList())
    }

    private fun executeWithParameters(sqlStatement: String, parameters: List<Any>): List<Map<String, String?>> {
        try {
            dataSource.connection.use { cxn ->
                val rows = mutableListOf<Map<String, String?>>()
                cxn.prepareStatement(sqlStatement).use { ps ->
                    setParameters(ps, parameters)
                    ps.executeQuery().use { rs ->
                        while (rs.next()) {
                            val values = mutableMapOf<String, String?>()
                            for (i in 1..rs.metaData.columnCount) {
                                values[rs.metaData.getColumnName(i).lowercase()] = rs.getString(i)
                            }
                            rows.add(values)
                        }
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
