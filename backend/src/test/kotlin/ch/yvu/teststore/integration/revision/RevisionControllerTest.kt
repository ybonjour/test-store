package ch.yvu.teststore.integration.revision

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.revision.Revision
import ch.yvu.teststore.revision.RevisionRepository
import com.jayway.restassured.RestAssured.given
import org.hamcrest.Description
import org.hamcrest.Matchers.hasItem
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.text.SimpleDateFormat
import java.util.*

class RevisionControllerTest : BaseIntegrationTest() {
    companion object {
        val now = Date(1)
        val isoFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        val nowString = SimpleDateFormat(isoFormat).format(now)
        val runId = UUID.randomUUID()
        val revision = "abc-123"
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
        given()
                .queryParam("time", nowString)
                .queryParam("revision", revision)
                .queryParam("author", author)
                .queryParam("comment", comment)
                .queryParam("url", url)
                .post("/runs/$runId/revisions")
                .then().statusCode(201)
    }

    @Test fun createRevisionStoresRevisionCorrectly() {
        given()
                .queryParam("time", nowString)
                .queryParam("revision", revision)
                .queryParam("author", author)
                .queryParam("comment", comment)
                .queryParam("url", url)
                .post("/runs/$runId/revisions")

        val revisions = revisionRepository.findAll()
        assertThat(revisions, hasItem(revisionWith(runId, now, revision, author, comment, url)))
    }

    @Test fun createRevisionStoresRunWithOnlyMandatoryFieldsCorrectly() {
        given()
                .queryParam("time", nowString)
                .queryParam("revision", revision)
                .post("/runs/$runId/revisions")

        val revisions = revisionRepository.findAll()
        assertThat(revisions, hasItem(revisionWith(runId, now, revision, null, null, null)))
    }

    @Test fun createRevisionReturns400StatusCodeIfNoTimeProvided() {
        given()
                .queryParam("revision", revision)
                .post("/runs/$runId/revisions")
                .then().statusCode(400)
    }

    @Test fun createRevisionRreturns400StatusCodeIfNoRevisionProvided() {
        given()
                .queryParam("time", nowString)
                .post("/runs/$runId/revisions")
                .then().statusCode(400)
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