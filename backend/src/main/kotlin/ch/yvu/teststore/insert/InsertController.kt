package ch.yvu.teststore.insert

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class InsertController @Autowired constructor(val insertService: InsertService) {

    @RequestMapping(
            method = arrayOf(POST),
            value = ["/runs/{run}/results"],
            headers = arrayOf("content-type=application/xml"))
    fun insert(@PathVariable run: UUID, @RequestBody body: String) {

        val resultDtos = JunitXMLParser.parse(body)

        insertService.insertResults(resultDtos, run)
    }

}