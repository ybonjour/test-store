package ch.yvu.teststore.run

import ch.yvu.teststore.common.Model
import org.springframework.data.cassandra.mapping.PrimaryKey
import org.springframework.data.cassandra.mapping.Table
import java.util.*

@Table("run")
data class Run(@PrimaryKey val id: UUID, val testSuite: String, val revision: String) : Model {
    override fun id(): UUID {
        return id
    }
}