package ch.yvu.teststore.history

import ch.yvu.teststore.common.Page
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import java.util.*

@RestController
class HistoryController @Autowired constructor(val historyService: HistoryService) {
    @RequestMapping(method = arrayOf(GET), value = ["/testsuites/{testSuite}/history/testnames"])
    fun getTestNames(
            @PathVariable testSuite: UUID,
            @RequestParam(value="limit") limit:Int): List<String> {
        return historyService.getAllTestnames(testSuite, limit);
    }

    // HACK: This is a POST method, since not all clients support a body in a GET request
    // A list of testnames is sent in the body to specify the order of the results
    // (without repeating the testnames for every run).
    // Need to find a better solution for that.
    @RequestMapping(
            method = arrayOf(POST),
            value = ["/testsuites/{testSuite}/history/results"],
            headers = arrayOf("content-type=application/json"))
    fun getResults(
            @PathVariable testSuite: UUID,
            @RequestParam(value = "page", required = false) page: String?,
            @RequestParam(value = "fetchSize", required = false) fetchSize: Int?,
            @RequestBody testnames: List<String>
    ): Page<RunHistory> {
        return historyService.getResultsForTests(testSuite, testnames, page, fetchSize)
    }
}