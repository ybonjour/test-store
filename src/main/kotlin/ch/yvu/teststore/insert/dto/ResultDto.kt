package ch.yvu.teststore.insert.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ResultDto(
        @JsonProperty("testName") val testName: String,
        @JsonProperty("retryNum") val retryNum: Int=0,
        @JsonProperty("passed") val passed: Boolean,
        @JsonProperty("durationSeconds") val durationSeconds: Long)