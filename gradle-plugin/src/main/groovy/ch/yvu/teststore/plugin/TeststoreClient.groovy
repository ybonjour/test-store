package ch.yvu.teststore.plugin

import ch.yvu.teststore.plugin.ScmChanges.ScmChange
import groovy.json.JsonBuilder
import groovy.text.SimpleTemplateEngine

import java.text.SimpleDateFormat

class TeststoreClient {
    HttpClient httpClient
    UUID testSuiteId

    private static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

    def createRun(String revision, Date time) {
        def timeString = new SimpleDateFormat(ISO_DATE_FORMAT).format(time)
        def tags = collectTags()

        def run = [revision: revision, time: timeString, tags: tags]
        def jsonBuilder = new JsonBuilder(run)

        def response = httpClient.postJson("/testsuites/${testSuiteId.toString()}/runs", jsonBuilder.toString())
        return UUID.fromString(response.id)
    }

    def insertScmChange(UUID runId, ScmChange scmChange, String urlTemplate) {
        def timeString = new SimpleDateFormat(ISO_DATE_FORMAT).format(scmChange.getTime())
        def revision = [
                revision: scmChange.getRevision(),
                time    : timeString,
                author  : scmChange.getAuthor(),
                comment : scmChange.getDescription()]

        if (urlTemplate != null) {
            revision.put("url", new SimpleTemplateEngine().createTemplate(urlTemplate).make([revision: scmChange.getRevision()]).toString())
        }

        def jsonBuilder = new JsonBuilder(revision)
        httpClient.postJson("/runs/${runId.toString()}/revisions", jsonBuilder.toString())
    }

    def insertTestResult(UUID runId, String junitXml) {
        httpClient.postXml("/runs/$runId/results", junitXml)
    }

    def insertTestResult(UUID runId, String testName, boolean passed, long durationMillis, Date time, String stackTrace, String log) {
        def timeString = new SimpleDateFormat(ISO_DATE_FORMAT).format(time);
        def result = [
                run           : runId.toString(),
                testName      : testName,
                retryNum      : 0,
                passed        : passed,
                durationMillis: durationMillis,
                time          : timeString,
                stackTrace    : stackTrace,
                log           : log
        ]
        def jsonBuilder = new JsonBuilder(result)

        httpClient.postJson("/runs/$runId/results", jsonBuilder.toString())
    }

    def collectTags() {
        def device = InetAddress.getLocalHost().getHostName()
        def tags = new HashMap<String, String>()
        tags.put("device", device)
        return tags
    }
}
