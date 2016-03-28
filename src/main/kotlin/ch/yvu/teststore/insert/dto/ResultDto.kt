package ch.yvu.teststore.insert.dto

data class ResultDto(val test: TestDto, val retryNum: Int=0, val passed: Boolean)