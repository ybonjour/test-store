package ch.yvu.teststore.history

import ch.yvu.teststore.common.Page
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.GET
import java.util.*

@RestController
class HistoryController @Autowired constructor(val historyService: HistoryService) {
    @RequestMapping(method = arrayOf(GET), value = "/testsuites/{testSuite}/history/testnames")
    fun getTestNames(
            @PathVariable testSuite: UUID,
            @RequestParam(value="limit") limit:Int): List<String> {
        return historyService.getAllTestnames(testSuite, limit);
    }

    @RequestMapping(method = arrayOf(GET), value = "/testsuites/{testSuite}/history/results")
    fun getResults(
            @PathVariable testSuite: UUID,
            @RequestParam(value = "page", required = false) page: String?,
            @RequestParam(value = "fetchSize", required = false) fetchSize: Int?,
            @RequestBody testnames: List<String>
    ): Page<RunHistory> {
        return historyService.getResultsForTests(testSuite, testnames, page, fetchSize)
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