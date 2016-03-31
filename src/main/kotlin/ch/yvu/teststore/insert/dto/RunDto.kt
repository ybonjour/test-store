package ch.yvu.teststore.insert.dto

import java.util.*

data class RunDto(val revision: String, val time: Date, val results: List<ResultDto> = emptyList())