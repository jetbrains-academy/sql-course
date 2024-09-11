data class EvaluationResult(val message: String, val expected: String?, val actual: String?){
    fun then(code: () -> EvaluationResult): EvaluationResult =
        if (this.expected == this.actual) {
            code()
        } else this
}
