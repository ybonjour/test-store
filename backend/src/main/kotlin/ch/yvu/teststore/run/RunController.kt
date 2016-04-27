package ch.yvu.teststore.run

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.UUID.randomUUID
import javax.servlet.http.HttpServletResponse

@RestController
class RunController @Autowired constructor(val runRepository: RunRepository) {

    @RequestMapping(method = arrayOf(POST), value = "/testsuites/{testSuite}/runs")
    fun createRun(
            @PathVariable testSuite: UUID,
            @RequestParam(name = "revision") revision: String,
            @RequestParam(name = "time") @DateTimeFormat(iso = ISO.DATE_TIME) time: Date,
            response: HttpServletResponse): Run {

        val run = Run(randomUUID(), testSuite, revision, time)
        runRepository.save(run)
        response.status = 201

        return run
    }

    @RequestMapping(method = arrayOf(GET), value = "/testsuites/{testSuite}/runs")
    fun findAllByTestSuite(@PathVariable testSuite: UUID): List<Run> {
        return runRepository.findAllByTestSuiteId(testSuite)
    }
}