package ch.yvu.teststore.testsuite

import ch.yvu.teststore.common.Model
import org.springframework.data.cassandra.mapping.PrimaryKey
import org.springframework.data.cassandra.mapping.Table
import java.util.*

@Table("testsuite")
data class TestSuite(@PrimaryKey val id: UUID, val name: String) : Model {
    override fun id(): UUID {
        return id
    }
}