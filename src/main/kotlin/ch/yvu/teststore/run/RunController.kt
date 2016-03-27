package ch.yvu.teststore.run

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID.randomUUID
import javax.servlet.http.HttpServletResponse

@RestController
class RunController @Autowired constructor(val runRepository: RunRepository) {

    @RequestMapping(method = arrayOf(POST), value = "/runs")
    fun createRun(
            @RequestParam(name = "revision") revision: String,
            @RequestParam(name = "testSuite") testSuite: String,
            response: HttpServletResponse):Run {

        val run = Run(randomUUID(), testSuite, revision)
        runRepository.save(run)
        response.status = 201

        return run
    }
}