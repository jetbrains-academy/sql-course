package com.jetbrains.edu.sql101

import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class QueryParameter
@Serializable
@SerialName("string")
data class QueryStringParameter(val value: String): QueryParameter()
@Serializable
@SerialName("int")
data class QueryIntParameter(val value: Int): QueryParameter()

@Serializable
data class QueryTestParameters(val queryNum: Int, val expectedResult: String, val placeholderValues: Map<String, QueryParameter> = emptyMap())

@Serializable
data class SolutionTestParameters(val solutionFile: String, val queryTestParameters: List<QueryTestParameters>)

@Serializable
@Resource("/assess")
class AssessSolutionHandler(val testParameters: SolutionTestParameters)
