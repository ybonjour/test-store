package ch.yvu.teststore.result

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
class ResultController @Autowired constructor(val resultRepository: ResultRepository, val resultService: ResultService) {

    @RequestMapping(method = arrayOf(POST), value = "/results")
    fun createResult(
            @RequestParam(name = "run") run: UUID,
            @RequestParam(name = "testName") testName: String,
            @RequestParam(name = "retryNum") retryNum: Int,
            @RequestParam(name = "passed") passed: Boolean,
            @RequestParam(name = "durationMillis") durationMillis: Long,
            response: HttpServletResponse): Result {
        val result = Result(run, testName, retryNum, passed, durationMillis)
        resultRepository.save(result)
        response.status = 201
        return result
    }

    @RequestMapping(method = arrayOf(GET), value = "/runs/{run}/results")
    fun getAllResults(@PathVariable run: UUID): List<Result> {
        return resultRepository.findAllByRunId(run)
    }

    @RequestMapping(method = arrayOf(GET), value = "/runs/{run}/results/grouped")
    fun getAllResultsGrouped(
            @PathVariable run: UUID): Map<TestWithResults.TestResult, List<TestWithResults>> {
        return resultService.getGroupedResults(run)
    }

}