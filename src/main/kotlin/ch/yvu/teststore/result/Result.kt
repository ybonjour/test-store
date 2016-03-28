package ch.yvu.teststore.result

import ch.yvu.teststore.common.Model
import org.springframework.data.cassandra.mapping.PrimaryKey
import org.springframework.data.cassandra.mapping.Table
import java.util.*

@Table("result")
data class Result(@PrimaryKey val id: UUID, val run: UUID, val test: UUID, val testName: String, val retryNum: Int, val passed: Boolean) : Model {
    override fun id(): UUID {
        return id
    }

}