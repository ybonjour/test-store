package ch.yvu.teststore.testsuite

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID.randomUUID
import javax.servlet.http.HttpServletResponse

@RestController
class TestSuiteController @Autowired constructor(val testSuiteRepository: TestSuiteRepository) {

    @RequestMapping(method = arrayOf(POST), value = "/testsuites")
    fun createTestSuite(@RequestParam(name = "name") name: String, response: HttpServletResponse): TestSuite {
        val testSuite = TestSuite(randomUUID().toString(), name)
        testSuiteRepository.save(testSuite)
        response.status = 201
        return testSuite
    }

}
