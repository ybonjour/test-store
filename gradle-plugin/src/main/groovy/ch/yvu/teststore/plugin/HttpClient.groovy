package ch.yvu.teststore.plugin

import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.ContentType.URLENC

class HttpClient {
    private HTTPBuilder http

    HttpClient(String host, int port) {
        http = new HTTPBuilder("http://${host}:${port}")
    }

    def postForm(String path, Map<String, String> parameters) {
        http.post(path: path, body: parameters, requestContentType: URLENC)
    }
}