package ch.yvu.teststore.testsuite

import ch.yvu.teststore.run.overview.RunOverview
import ch.yvu.teststore.run.overview.RunOverview.RunResult.UNKNOWN
import ch.yvu.teststore.run.overview.RunOverviewService
import ch.yvu.teststore.testsuite.overview.TestSuiteOverview
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID.randomUUID
import javax.servlet.http.HttpServletResponse

@RestController
class TestSuiteController @Autowired constructor(val testSuiteRepository: TestSuiteRepository, val runOverviewService: RunOverviewService) {

    @RequestMapping(method = arrayOf(POST), value = "/testsuites")
    fun createTestSuite(@RequestParam(name = "name") name: String, response: HttpServletResponse): TestSuite {
        val testSuite = TestSuite(randomUUID(), name)
        testSuiteRepository.save(testSuite)
        response.status = 201
        return testSuite
    }

    @RequestMapping(method = arrayOf(GET), value = "/testsuites")
    fun getAllTestSuites(): List<TestSuiteOverview> {
        return testSuiteRepository.findAll().map {
            val runOverview = runOverviewService.getLastRunOverview(it.id!!);
            val runResult = if(runOverview.isPresent) runOverview.get().result else UNKNOWN;
            TestSuiteOverview(it, runResult)
        };
    }
}
