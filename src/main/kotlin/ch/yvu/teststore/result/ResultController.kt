package ch.yvu.teststore.result

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.UUID.randomUUID
import javax.servlet.http.HttpServletResponse

@RestController
class ResultController @Autowired constructor(val resultRepository: ResultRepository) {

    @RequestMapping(method = arrayOf(POST), value = "/results")
    fun createResult(
            @RequestParam(name = "run") run: UUID,
            @RequestParam(name = "test") test: UUID,
            @RequestParam(name = "testName") testName: String,
            @RequestParam(name = "retryNum") retryNum: Int,
            @RequestParam(name = "passed") passed: Boolean,
            @RequestParam(name = "durationSeconds") durationSeconds: Long,
            response: HttpServletResponse): Result {
        val result = Result(randomUUID(), run, testName, retryNum, passed, durationSeconds)
        resultRepository.save(result)
        response.status = 201
        return result
    }
}