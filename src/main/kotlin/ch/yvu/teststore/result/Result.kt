package ch.yvu.teststore.result

import ch.yvu.teststore.common.Model
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import java.util.*

@Table(name = "result")
data class Result(@PartitionKey var id: UUID, var run: UUID, var testName: String, var retryNum: Int, var passed: Boolean, var durationMillis: Long) : Model