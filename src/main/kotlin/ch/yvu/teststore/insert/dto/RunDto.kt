package ch.yvu.teststore.insert.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class RunDto @JsonCreator constructor(
        @JsonProperty("revision") val revision: String,
        @JsonProperty("time") val time: Date,
        @JsonProperty("results") val results: List<ResultDto> = emptyList())