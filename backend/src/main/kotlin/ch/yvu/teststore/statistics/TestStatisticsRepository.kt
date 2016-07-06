package ch.yvu.teststore.statistics

import ch.yvu.teststore.common.TestStoreRepository
import org.springframework.stereotype.Repository

@Repository
interface TestStatisticsRepository : TestStoreRepository<TestStatistics> {

}