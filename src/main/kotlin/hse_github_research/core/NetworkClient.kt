package hse_github_research.core

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

private const val GITHUB_API_HOST = "api.github.com"
private const val TOKEN = "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" // ;)

private const val REQUEST_TIMEOUT_MILLIS = 300_000L

object NetworkClient {
    val githubClient =
        HttpClient(CIO) {
            expectSuccess = false

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }

            install(JsonFeature) {
                serializer =
                    KotlinxSerializer(kotlinx.serialization.json.Json { ignoreUnknownKeys = true })
            }

            install(HttpTimeout) { requestTimeoutMillis = REQUEST_TIMEOUT_MILLIS }

            defaultRequest {
                host = GITHUB_API_HOST
                headers { append(HttpHeaders.Authorization, "token $TOKEN") }
                url { protocol = URLProtocol.HTTPS }
            }
        }
}
