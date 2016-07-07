package ch.yvu.teststore.statistics

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class StatisticsController @Autowired constructor(val testStatisticsRepository: TestStatisticsRepository){

    @RequestMapping(method = arrayOf(GET), value = "/testsuites/{testSuite}/statistics")
    fun getStatisticsByTestSuite(
            @PathVariable testSuite: UUID
    ): List<TestStatistics> {
        return testStatisticsRepository.findAllByTestSuite(testSuite)
    }

}