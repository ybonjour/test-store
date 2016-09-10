package ch.yvu.teststore.run

import ch.yvu.teststore.common.CassandraRepository
import ch.yvu.teststore.common.Page
import ch.yvu.teststore.common.QueryFactory
import com.datastax.driver.mapping.MappingManager
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

open class CassandraRunRepository @Autowired constructor(mappingManager: MappingManager, val queryFactory: QueryFactory) :
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

    override fun findAllByTestSuiteId(testSuiteId: UUID, maxRows: Int): List<Run> {
        val query = queryFactory.createQuery("SELECT * FROM run WHERE testSuite=?", testSuiteId)
        return maxRowsResultFetcher.fetch(query, maxRows)
    }

    override fun findAllByTestSuiteId(testSuiteId: UUID, page: String?): Page<Run> {
        val query = queryFactory.createQuery("SELECT * FROM run WHERE testSuite=?", testSuiteId)
        return pagedResultFetcher.fetch(query, page)
    }
}

