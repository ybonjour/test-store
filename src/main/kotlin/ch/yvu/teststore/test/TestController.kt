package ch.yvu.teststore.test

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID.randomUUID

@RestController
class TestController {
    @RequestMapping("/tests")
    fun tests(): List<Test> {
        return listOf(Test(randomUUID(), "fooTest"), Test(randomUUID(), "barTest"))
    }
}