package ch.yvu.teststore.result

import ch.yvu.teststore.common.Model
import java.util.*

data class Result(val id: UUID, val run: UUID, val test: UUID, val retryNum: Int, val passed: Boolean) : Model {
    override fun id(): UUID {
        return id
    }

}