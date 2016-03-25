package ch.yvu.teststore.test

import org.springframework.data.cassandra.mapping.PrimaryKey
import org.springframework.data.cassandra.mapping.Table
import java.util.*

@Table(value = "test")
data class Test(@PrimaryKey val id:String, val name: String)