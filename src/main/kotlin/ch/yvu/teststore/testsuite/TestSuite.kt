package ch.yvu.teststore.testsuite

import ch.yvu.teststore.common.Model
import org.springframework.data.cassandra.mapping.PrimaryKey
import org.springframework.data.cassandra.mapping.Table

@Table("testsuite")
data class TestSuite(@PrimaryKey val id: String, val name: String) : Model {
    override fun id(): String {
        return id
    }
}