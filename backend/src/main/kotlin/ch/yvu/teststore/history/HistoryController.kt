package ch.yvu.teststore.history

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class HistoryController @Autowired constructor(val historyService: HistoryService) {
    @RequestMapping(method = arrayOf(GET), value = "/testsuites/{testSuite}/history/testnames")
    fun getTestNames(
            @PathVariable testSuite: UUID,
            @RequestParam(value="limit") limit:Int): List<String> {
        return historyService.getAllTestnames(testSuite, limit);
    }

    @RequestMapping(method = arrayOf(GET), value = "/testsuites/{testSuite}/history")
    fun getHistoryNew(
            @PathVariable testSuite: UUID,
            @RequestParam(value = "limit") limit: Int): TestnamesAndResults {

        val testnames = historyService.getAllTestnames(testSuite, limit)
        val runHistory = historyService.getResultsForTests(testSuite, testnames, limit)
        return TestnamesAndResults(testnames = testnames, runHistory = runHistory)
    }
}