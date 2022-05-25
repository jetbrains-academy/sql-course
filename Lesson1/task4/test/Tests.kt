import org.junit.Assert
import org.junit.Test

class Test {
    @Test fun testSolution() {
        task4().let {
            Assert.assertTrue("Country name looks wrong", "albania" == it.first.lowercase())
            Assert.assertTrue("Death rate looks wrong", 107.2 == it.second)
        }
    }
}