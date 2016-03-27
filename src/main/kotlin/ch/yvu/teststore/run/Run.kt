package ch.yvu.teststore.run

import ch.yvu.teststore.common.Model
import org.springframework.data.cassandra.mapping.PrimaryKey
import org.springframework.data.cassandra.mapping.Table

@Table("run")
data class Run(@PrimaryKey val id: String, val testSuite: String, val revision: String) : Model {
    override fun id(): String {
        return id
    }
}