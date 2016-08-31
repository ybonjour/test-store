package ch.yvu.teststore.run

import ch.yvu.teststore.common.CassandraRepository
import ch.yvu.teststore.common.Page
import com.datastax.driver.core.PagingState
import com.datastax.driver.core.SimpleStatement
import com.datastax.driver.mapping.MappingManager
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

open class CassandraRunRepository @Autowired constructor(mappingManager: MappingManager) :
        RunRepository, CassandraRepository<Run>(mappingManager, "run", Run::class.java) {

    override fun save(item: Run): Run {
        session.execute("INSERT INTO runById (id, testsuite, revision, time) VALUES(?, ?, ?, ?)",
                item.id, item.testSuite, item.revision, item.time)
        return super.save(item)
    }

    override fun findById(id: UUID): Run? {
        val resultSet = session.execute("SELECT * FROM runById WHERE id=?", id)
        if(resultSet.isExhausted) return null
        return mapper.map(resultSet).one()
    }

    override fun findLastRunBefore(testSuiteId: UUID, time: Date): Run? {
        val resultSet = session.execute("select * from run where testsuite=? and time < ? LIMIT 1", testSuiteId, time)
        val result = mapper.map(resultSet)
        if(result.isExhausted) return null
        else return result.one()
    }

    override fun findAllByTestSuiteId(testSuiteId: UUID): List<Run> {
        val resultSet = session.execute("SELECT * FROM run WHERE testSuite=?", testSuiteId)
        return mapper.map(resultSet).all().toList()
    }


    override fun findAllByTestSuiteIdPaged(testSuiteId: UUID, page: String?): Page<Run> {
        val statement = SimpleStatement("SELECT * FROM run WHERE testSuite=?", testSuiteId)
        statement.fetchSize = 2
        if(page != null) {
            statement.setPagingState(PagingState.fromString(page))
        }

        val resultSet = session.execute(statement)
        val remaining = resultSet.availableWithoutFetching
        val results = mapper.map(resultSet)

        var runs = emptyList<Run>();
        (1..remaining).forEach { runs += results.one() }

        return Page(runs, resultSet.executionInfo?.pagingState?.toString())
    }

}

