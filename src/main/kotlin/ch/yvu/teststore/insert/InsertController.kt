package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.RunDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
class InsertController @Autowired constructor(val insertService: InsertService) {

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/testsuites/{testSuite}/runs")
    fun insert(@PathVariable testSuite: UUID, @RequestBody runDto: RunDto, response: HttpServletResponse) {
        insertService.insertRun(runDto, testSuite)
        response.status = 200
    }

}