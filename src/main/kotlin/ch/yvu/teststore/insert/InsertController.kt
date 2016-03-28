package ch.yvu.teststore.insert

import ch.yvu.teststore.run.RunRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
class InsertController @Autowired constructor(val runRespository: RunRepository, val runRepository: RunRepository) {

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/insert")
    fun insert( response: HttpServletResponse) {
        response.status = 200
    }

}