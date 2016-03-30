package ch.yvu.teststore.testsuite

import ch.yvu.teststore.common.Model
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import java.util.*

@Table(name = "testsuite")
data class TestSuite(@PartitionKey var id: UUID, var name: String) : Model