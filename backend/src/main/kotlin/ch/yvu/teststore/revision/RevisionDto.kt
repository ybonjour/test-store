package ch.yvu.teststore.revision

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class RevisionDto @JsonCreator constructor(
        @JsonProperty("revision") val revision: String,
        @JsonProperty("time") val time: Date,
        @JsonProperty(value = "author", required = false) val author: String? = null,
        @JsonProperty(value = "comment", required = false) val comment: String? = null,
        @JsonProperty(value = "url", required = false) val url: String? = null)