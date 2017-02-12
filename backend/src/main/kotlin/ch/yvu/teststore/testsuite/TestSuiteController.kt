package ch.yvu.teststore.testsuite

import ch.yvu.teststore.insert.dto.TestSuiteDto
import ch.yvu.teststore.run.overview.RunOverviewService
import ch.yvu.teststore.run.overview.RunStatistics.RunResult.UNKNOWN
import ch.yvu.teststore.testsuite.overview.TestSuiteOverview
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.UUID.randomUUID
import javax.servlet.http.HttpServletResponse

@RestController
class TestSuiteController @Autowired constructor(val testSuiteRepository: TestSuiteRepository, val runOverviewService: RunOverviewService) {

    @RequestMapping(
            method = arrayOf(POST),
            value = "/testsuites",
            headers = arrayOf("content-type=application/json"))
    fun createTestSuiteJson(@RequestBody testSuiteDto: TestSuiteDto, response: HttpServletResponse): TestSuite {
        val testSuite = TestSuite(randomUUID(), testSuiteDto.name)
        testSuiteRepository.save(testSuite)
        response.status = 201
        return testSuite
    }

    @RequestMapping(method = arrayOf(GET), value = "/testsuites")
    fun getAllTestSuites(): List<TestSuiteOverview> {
        return testSuiteRepository.findAll().map {
            val runOverview = runOverviewService.getLastRunOverview(it.id!!);
            val runResult = if (runOverview.isPresent) runOverview.get().runStatistics.result else UNKNOWN;
            TestSuiteOverview(it, runResult)
        };
    }

    @RequestMapping(method = arrayOf(GET), value = "/testsuites/raw")
    fun getAllTestSuitesRaw(): List<TestSuite> {
        return testSuiteRepository.findAll();
    }

    @RequestMapping(method = arrayOf(GET), value = "/testsuites/{testSuiteId}")
    fun getTestSuite(@PathVariable testSuiteId: UUID): ResponseEntity<TestSuiteOverview> {
        val testSuite = testSuiteRepository.findById(testSuiteId)
        if (testSuite == null) return ResponseEntity(NOT_FOUND)

        val runOverview = runOverviewService.getLastRunOverview(testSuite.id!!)
        val runResult = if (runOverview.isPresent) runOverview.get().runStatistics.result else UNKNOWN;
        return ResponseEntity(TestSuiteOverview(testSuite, runResult), OK)
    }
}
