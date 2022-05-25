import org.h2.jdbcx.JdbcDataSource
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

    fun executeFromFile(resourceName: String): List<Map<String, String>> {
        val input = javaClass.getResourceAsStream(resourceName) ?: return emptyList()
        return input.reader().readLines().firstOrNull { !it.trim().startsWith("--") }?.let {
            execute(it)
        } ?: emptyList()
    }

    fun execute(sqlStatement: String): List<Map<String, String>> {
        try {
            dataSource.connection.use { cxn ->
                val rows = mutableListOf<Map<String, String>>()
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