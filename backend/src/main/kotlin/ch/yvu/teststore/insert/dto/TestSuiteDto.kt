package ch.yvu.teststore.insert.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class TestSuiteDto(
        @JsonProperty("name") val name: String)