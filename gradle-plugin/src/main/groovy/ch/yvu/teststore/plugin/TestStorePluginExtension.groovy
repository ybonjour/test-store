package ch.yvu.teststore.plugin

import java.util.regex.Pattern

class TestStorePluginExtension {
    def String host = 'localhost'
    def int port = 8080
    def Pattern xmlReports = ~/.*\/test-results\/.*\.xml/
    def String testSuite
    def String revision = ""
    def boolean incremental = false
    def String changesFile = ""
}
