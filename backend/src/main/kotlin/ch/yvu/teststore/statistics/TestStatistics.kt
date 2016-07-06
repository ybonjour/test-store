package ch.yvu.teststore.statistics

import ch.yvu.teststore.common.Model
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import java.util.*

@Table(name = "test_statistics")
data class TestStatistics(
        @PartitionKey(0) var testSuite: UUID?,
        @PartitionKey(1) var testName: String?,
        var numPassed: Int?,
        var numFailed: Int?): Model {
    constructor(): this(null, null, null, null)
}
