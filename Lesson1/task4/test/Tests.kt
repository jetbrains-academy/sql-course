import org.junit.Assert
import org.junit.Test

class Test {
    @Test fun testSolution() {
        task4().let {
            Assert.assertTrue("Airport code looks wrong", "cdg" == it.first.lowercase())
            Assert.assertTrue("Number of movements looks wrong", 475776 == it.second)
        }
    }
}