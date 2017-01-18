package ch.yvu.teststore.revision

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
class RevisionController @Autowired constructor(val revisionRepository: RevisionRepository) {

    @RequestMapping(
            method = arrayOf(POST),
            value = "/runs/{run}/revisions",
            headers = arrayOf("content-type=application/json"))
    fun createRevision(
            @PathVariable run: UUID,
            @RequestBody revisionDto: RevisionDto,
            response: HttpServletResponse) {
        val revision = Revision(run, revisionDto.time, revisionDto.revision, revisionDto.author, revisionDto.comment, revisionDto.url)
        revisionRepository.save(revision)
        response.status = 201
    }

    @RequestMapping(method = arrayOf(GET), value = "/runs/{run}/revisions")
    fun getRevisionsByRun(@PathVariable run: UUID): List<Revision> {
        return revisionRepository.findAllByRunId(run)
    }
}