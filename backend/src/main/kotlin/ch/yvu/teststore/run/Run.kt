package ch.yvu.teststore.run

import ch.yvu.teststore.common.Model
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import java.util.*

@Table(name = "run")
data class Run(
        @PartitionKey(2) var id: UUID?,
        @PartitionKey(0) var testSuite: UUID?,
        var revision: String?,
        @PartitionKey(1) var time: Date?) : Model {
    constructor(): this(null, null, null, null) {}
}