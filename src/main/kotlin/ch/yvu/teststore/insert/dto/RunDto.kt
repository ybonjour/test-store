package ch.yvu.teststore.insert.dto

import com.google.gson.GsonBuilder
import java.util.*

data class RunDto(val revision: String, val time: Date, val results: List<ResultDto> = emptyList()) {
    companion object {
        fun fromJson(json: String): RunDto {
            return GsonBuilder().create().fromJson(json, RunDto::class.java)
        }
    }
}