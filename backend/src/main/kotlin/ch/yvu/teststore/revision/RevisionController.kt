package ch.yvu.teststore.revision

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.POST
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
class RevisionController @Autowired constructor(val revisionRepository: RevisionRepository) {

    @RequestMapping(method = arrayOf(POST), value="/runs/{run}/revisions")
    fun createRevision(
            @PathVariable run: UUID,
            @RequestParam(name = "time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) time: Date,
            @RequestParam(name = "revision") revision: String,
            @RequestParam(name = "author", required = false) author: String?,
            @RequestParam(name = "comment", required = false) comment: String?,
            @RequestParam(name = "url", required = false) url: String?,
            response: HttpServletResponse) {
        val revision = Revision(run, time, revision, author, comment, url)
        revisionRepository.save(revision)
        response.status = 201
    }
}