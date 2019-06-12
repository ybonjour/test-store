package ch.yvu.teststore.insert.dto

import java.util.*

fun runDtoInstance(
        revision: String? = "abc123",
        time: Date = Date(1),
        tags: Map<String, String> = emptyMap()
) = RunDto(revision = revision, time = time, tags = tags)