package ch.yvu.teststore.result

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
class ResultController {

    @RequestMapping(method = arrayOf(POST), value = "/results")
    fun createResult(
            @RequestParam(name = "run") run: UUID,
            @RequestParam(name="test") test: UUID,
            @RequestParam(name = "retryNum") retryNum: Int,
            @RequestParam(name = "passed") passed: Boolean,
            response:HttpServletResponse) {
            response.status = 201
    }
}