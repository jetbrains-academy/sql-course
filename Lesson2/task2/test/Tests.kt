import org.junit.Assert
import org.junit.Before
import org.junit.Test

class Test {
    lateinit var db: Db
    @Before
    fun setUp() {
        db = Db("Task3")
    }

    @Test fun testSolution() {
        Assert.assertEquals(mutableListOf(
                mutableMapOf("id" to "1", "name" to "The Earth")),
                db.executeFromFile("/Query.sql"))
    }
}