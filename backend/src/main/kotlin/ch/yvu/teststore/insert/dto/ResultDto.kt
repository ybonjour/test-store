package ch.yvu.teststore.insert.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ResultDto(
        @JsonProperty("testName") val testName: String,
        @JsonProperty("retryNum") val retryNum: Int=0,
        @JsonProperty("passed") val passed: Boolean,
        @JsonProperty("durationMillis") val durationMillis: Long,
        @JsonProperty("time") val time: Date,
        @JsonProperty(value="stackTrace", required = false) val stackTrace: String?=null,
        @JsonProperty(value="log", required = false) val log: String?=null)