package ch.yvu.teststore.insert.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class RunDto @JsonCreator constructor(
        @JsonProperty("revision") val revision: String? = null,
        @JsonProperty("time") val time: Date,
        @JsonProperty("tags") val tags: Map<String, String>)