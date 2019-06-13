package ch.yvu.teststore.integration.run

import ch.yvu.teststore.run.Run
import java.util.*

fun runInstance(
        id: UUID? = UUID.fromString("91fc5b97-4bbd-4bac-a123-af483e1442c9"),
        testSuite: UUID? = UUID.fromString("06d9fd0f-5e9f-437d-9320-6e5b6eaef62f"),
        revision: String? = "abcd12345abcd",
        time: Date? = Date(1),
        tags: Map<String, String> = emptyMap()
) = Run(
        id = id,
        testSuite = testSuite,
        revision = revision,
        time = time,
        tags = tags
)