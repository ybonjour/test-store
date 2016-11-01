package ch.yvu.teststore.plugin

import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.XML

class HttpClient {
    private HTTPBuilder http

    HttpClient(String host, int port) {
        http = new HTTPBuilder("http://${host}:${port}")
    }

    def postJson(String path, String json) {
        return http.post(path: path, body: json, requestContentType: JSON)
    }

    def postForm(String path, Map<String, String> parameters) {
        return http.post(path: path, body: parameters)
    }

    def postXml(String path, String xml) {
        return http.post(path: path, body: xml, requestContentType: XML)
    }
}