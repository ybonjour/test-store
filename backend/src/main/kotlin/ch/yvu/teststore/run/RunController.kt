package ch.yvu.teststore.run

import ch.yvu.teststore.common.Page
import ch.yvu.teststore.insert.dto.RunDto
import ch.yvu.teststore.run.overview.RunOverview
import ch.yvu.teststore.run.overview.RunOverviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import java.util.*
import java.util.UUID.randomUUID
import javax.servlet.http.HttpServletResponse

@RestController
class RunController @Autowired constructor(val runRepository: RunRepository, val runOverviewService: RunOverviewService) {

    @RequestMapping(
            method=arrayOf(POST),
            value = "/testsuites/{testSuite}/runs/sync",
            headers = arrayOf("content-type=application/json"))
    fun createRunJson(
            @PathVariable testSuite: UUID,
            @RequestBody runDto: RunDto,
            response: HttpServletResponse
    ): Run {
        val run = Run(randomUUID(), testSuite, runDto.revision, runDto.time)
        runRepository.save(run)
        response.status = 201

        return run
    }

    @RequestMapping(method = arrayOf(GET), value = "/testsuites/{testSuite}/runs/last")
    fun getLastRunOverview(@PathVariable testSuite: UUID): ResponseEntity<RunOverview> {
        val lastRun = runOverviewService.getLastRunOverview(testSuite)
        if(!lastRun.isPresent){
            return ResponseEntity<RunOverview>(NOT_FOUND)
        }

        return ResponseEntity(lastRun.get(), OK)
    }

    @RequestMapping(method = arrayOf(GET), value = "testsuites/{testSuite}/runs/overview")
    fun getRunOverviewsPaged(
            @PathVariable testSuite: UUID,
            @RequestParam(name = "page", required=false) page: String?): Page<RunOverview> {
        return runOverviewService.getRunOverviews(testSuite, page)
    }
}