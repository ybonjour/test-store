package ch.yvu.teststore.revision

import ch.yvu.teststore.common.Model
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import java.util.*

@Table(name = "revision")
data class Revision(
        @PartitionKey(0) var run: UUID?,
        @PartitionKey(1) var time: Date?,
        @PartitionKey(2) var revision: String?,
        var author: String?,
        var comment: String?,
        var url: String?
) : Model {
    constructor(): this(null, null, null, null, null, null) {}
}