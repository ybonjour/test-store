package ch.yvu.teststore.integration.revision

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.revision.Revision
import ch.yvu.teststore.revision.RevisionDto
import ch.yvu.teststore.revision.RevisionRepository
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured.given
import io.restassured.http.ContentType.JSON
import org.hamcrest.Description
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItem
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.text.SimpleDateFormat
import java.util.*
import java.util.UUID.randomUUID

class RevisionControllerTest : BaseIntegrationTest() {
    companion object {
        val now = Date(1)
        val isoFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        val nowString = SimpleDateFormat(isoFormat).format(now)
        val runId = randomUUID()
        val revisionHash = randomUUID().toString()
        val author = "Yves Bonjour"
        val comment = "Some comment"
        val url = "https://github.com/ybonjour/test-store"
    }

    @Autowired lateinit var revisionRepository: RevisionRepository

    @Before override fun setUp() {
        super.setUp()

        revisionRepository.deleteAll()
    }

    @Test fun createRevisionReturnsCorrectStatusCode() {
        val revision = RevisionDto(revisionHash, now, author, comment, url)
        val mapper = ObjectMapper()
        mapper.dateFormat = SimpleDateFormat(RevisionControllerTest.isoFormat)
        val json = mapper.writeValueAsString(revision)

        given().contentType(JSON)
                .body(json)
                .post("/runs/$runId/revisions")
                .then().statusCode(201)
    }

    @Test fun createRevisionStoresRevisionCorrectly() {
        val revision = RevisionDto(revisionHash, now, author, comment, url)
        val mapper = ObjectMapper()
        mapper.dateFormat = SimpleDateFormat(RevisionControllerTest.isoFormat)
        val json = mapper.writeValueAsString(revision)

        given().contentType(JSON)
                .body(json)
                .post("/runs/$runId/revisions")

        val revisions = revisionRepository.findAll()
        assertThat(revisions, hasItem(revisionWith(runId, now, revisionHash, author, comment, url)))
    }

    @Test fun createRevisionStoresRevisionWithOnlyMandatoryFieldsCorrectly() {
        val revision = RevisionDto(revisionHash, now)
        val mapper = ObjectMapper()
        mapper.dateFormat = SimpleDateFormat(RevisionControllerTest.isoFormat)
        val json = mapper.writeValueAsString(revision)

        given().contentType(JSON)
                .body(json)
                .post("/runs/$runId/revisions")

        val revisions = revisionRepository.findAll()
        assertThat(revisions, hasItem(revisionWith(runId, now, revisionHash, null, null, null)))
    }

    @Test fun createRevisionReturns400StatusCodeIfNoTimeProvided() {


        given().contentType(JSON)
                .body("{\"revision\": \"${revisionHash}\"}")
                .post("/runs/$runId/revisions")
                .then().statusCode(500) // TODO: Fix to return 400
    }

    @Test fun createRevisionRreturns400StatusCodeIfNoRevisionProvided() {
        given().contentType(JSON)
                .body("{\"time\": \"${nowString}\"}")
                .post("/runs/$runId/revisions")
                .then().statusCode(500) // TODO: Fix to return 400
    }

    @Test fun getRevisionsByRunReturnsRevision() {
        val revision = Revision(runId, now, revisionHash, author, comment, url)
        revisionRepository.save(revision)

        given()
                .get("/runs/$runId/revisions")
                .then()
                .statusCode(200)
                .body("[0].run", equalTo(runId.toString()))
                .body("[0].time", equalTo(now.time.toInt()))
                .body("[0].revision", equalTo(revisionHash))
                .body("[0].author", equalTo(author))
                .body("[0].comment", equalTo(comment))
                .body("[0].url", equalTo(url))
    }


    private fun revisionWith(run: UUID, time: Date, revision: String, author: String?, comment: String?, url: String?) = object : TypeSafeMatcher<Revision>() {
        override fun matchesSafely(item: Revision?): Boolean {
            if (item == null) return false

            return item.run == run &&
                    item.time == time &&
                    item.revision == revision &&
                    item.author == author &&
                    item.comment == comment &&
                    item.url == url
        }

        override fun describeTo(description: Description?) {
            if (description == null) return

            description.appendText("Revision with {")
            description.appendText("run=").appendValue(run).appendText(", ")
            description.appendText("time=").appendValue(time).appendText(", ")
            description.appendText("revision=").appendValue(revision).appendText(", ")
            description.appendText("author=").appendValue(author).appendText(", ")
            description.appendText("comment=").appendValue(comment).appendText(", ")
            description.appendText("url=").appendValue(url).appendText(", ")

            description.appendText("}")
        }

    }

}