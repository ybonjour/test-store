package ch.yvu.teststore.test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID.randomUUID

@RestController
class TestController @Autowired constructor(val testRepository: TestRepository) {

    @RequestMapping(method = arrayOf(GET), value = "/tests")
    fun tests(): List<Test> {
        return testRepository.findAll().toList()
    }

    @RequestMapping(method = arrayOf(POST), value = "/tests")
    fun createTest(@RequestParam(name = "name") name: String) {
        val test = Test(randomUUID(), name)
        testRepository.save(test)
    }
}