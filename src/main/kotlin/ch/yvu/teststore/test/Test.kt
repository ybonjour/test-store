package ch.yvu.teststore.test

import ch.yvu.teststore.common.Model
import org.springframework.data.cassandra.mapping.PrimaryKey
import org.springframework.data.cassandra.mapping.Table
import java.util.*

@Table("test")
data class Test(@PrimaryKey val id: UUID, val name: String) : Model {
    override fun id(): UUID {
        return id
    }
}