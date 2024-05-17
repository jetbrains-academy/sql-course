import org.junit.Assert

data class ScoredSolution(val solution: String, val assessment: String, val score: Double) {
    fun then(code: () -> ScoredSolution): ScoredSolution =
            if (this.score == 1.0) {
                code()
            } else this
}

data class EvaluationResult(val message: String, val expected: String?, val actual: String?){
    fun then(code: () -> EvaluationResult): EvaluationResult =
        if (this.expected == this.actual) {
            code()
        } else this
}

fun printAssessments(solutions: List<ScoredSolution>) {
    Assert.fail(solutions.map { """${it.solution}
        |-------------------
        |${it.assessment}
        |
        |
    """.trimMargin() }.joinToString(separator = "\n"))
}
