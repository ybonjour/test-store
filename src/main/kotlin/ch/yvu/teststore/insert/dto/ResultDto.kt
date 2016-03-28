package ch.yvu.teststore.insert.dto

data class ResultDto(val testName: String, val retryNum: Int=0, val passed: Boolean)