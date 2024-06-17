package br.com.mtanuri.liferay.lcap.script.groovy

import java.net.HttpURLConnection
import java.net.URI
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Map

abstract class BaseScript extends Script {

    static class RestRequestBuilder {
        String url
        String method = 'GET'
        Map<String, String> headers = [:]
        def body = null

        RestRequestBuilder url(String url) {
            this.url = url
            return this
        }

        RestRequestBuilder method(String method) {
            this.method = method
            return this
        }

        RestRequestBuilder headers(Map<String, String> headers) {
            this.headers = headers
            return this
        }

        RestRequestBuilder body(def body) {
            this.body = body
            return this
        }

        def execute() {
            try {
                URI uri = new URI(url)
                HttpURLConnection connection = uri.toURL().openConnection()
                connection.setRequestMethod(method)

                headers.each { key, value ->
                    connection.setRequestProperty(key, value)
                }

                if (body != null) {
                    connection.doOutput = true
                    connection.setRequestProperty("Content-Type", "application/json")
                    OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream())
                    wr.write(body.toString())
                    wr.flush()
                }

                def response = new StringBuilder()
                BufferedReader _in = new BufferedReader(new InputStreamReader(connection.getInputStream()))
                String inputLine
                while ((inputLine = _in.readLine()) != null) {
                    response.append(inputLine)
                }
                        _in.close()

                connection.disconnect()

                return response.toString()
            } catch (Exception ex) {
                println "Error executing request: ${ex.message}"
                throw ex
            }
        }
    }

    def request() {
        return new RestRequestBuilder()
    }

    def request(url) {
        def builder = new RestRequestBuilder()
        builder.url(url)
        return builder
    }
}
