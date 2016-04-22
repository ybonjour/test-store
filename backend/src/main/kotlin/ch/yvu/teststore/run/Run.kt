package ch.yvu.teststore.run

import ch.yvu.teststore.common.Model
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import java.util.*

@Table(name = "run")
data class Run(
        @PartitionKey var id: UUID?,
        var testSuite: UUID?,
        var revision: String?,
        var time: Date?) : Model {
    constructor(): this(null, null, null, null) {}
}