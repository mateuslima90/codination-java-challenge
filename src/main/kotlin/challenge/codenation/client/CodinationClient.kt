package challenge.codenation.client

import challenge.codenation.model.Challenge
import challenge.codenation.model.ChallengeResult
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.core.io.buffer.ByteBuffer
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.multipart.MultipartBody
import io.micronaut.http.uri.UriBuilder
import io.reactivex.Flowable
import java.io.File
import java.io.FileWriter
import java.net.URI
import javax.inject.Singleton

@Singleton
class CodinationClient(@param:Client("https://api.codenation.dev") private val http: RxHttpClient) {

    fun getChallenge(token: String): Flowable<Challenge> {

        val path = "/v1/challenge/dev-ps/generate-data"

        val uri : URI = UriBuilder.of(path).queryParam("token", token).build()

        return http.exchange(HttpRequest.GET<Any>(uri), Challenge::class.java)
                .map { response -> response.body() }
    }

    fun sentChallenge(token: String, content: Challenge): Flowable<ChallengeResult?> {

        val path = "https://api.codenation.dev/v1/challenge/dev-ps/submit-solution"

        val uri : URI = UriBuilder.of(path).queryParam("token", token).build()

        val file = makingfile(content)

        val requestBody = MultipartBody.builder()
                .addPart("answer", file.name, MediaType.TEXT_PLAIN_TYPE, file)
                .build()

        return http.exchange(HttpRequest.POST(uri, requestBody)
                .contentType(MediaType.MULTIPART_FORM_DATA_TYPE), ChallengeResult::class.java)
                .map { requestBody -> requestBody.body() }
    }

    fun makingfile(content: Challenge) : File {

        val mapper = ObjectMapper()

        val json = mapper.writeValueAsString(content)

        val file = File.createTempFile("answer", ".json")
        val writer = FileWriter(file)
        writer.write(json)
        writer.close()

        file.forEachLine { println(it) }

        return file
    }
}