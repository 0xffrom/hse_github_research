package hse_github_research.core

import Api
import ApiImpl
import hse_github_research.GITHUB_TOKEN_1
import hse_github_research.GITHUB_TOKEN_2
import hse_github_research.models.github.GithubResource
import hse_github_research.models.github.GithubResource.Companion.DEFAULT_VALUE
import hse_github_research.models.github.ResourceType
import hse_github_research.models.github.limit.Resources
import hse_github_research.models.github.limit.getByType
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

data class ApiClient(val api: Api, val client: HttpClient)

@Suppress("UNCHECKED_CAST")
class GithubProxyNetworkClient {

    init {
        CLIENTS.forEach { apiClient -> resources[apiClient] = Resources() }
    }

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    suspend inline fun <reified Response> response(
        resourceType: ResourceType,
        apiFunction: Api.() -> HttpResponse,
    ): Response? {
        val client = client.getClientBlocked(resources, resourceType)
        val httpResponse = apiFunction.invoke(client.api)
        client.updateLimits(httpResponse)

        val responseText = httpResponse.readText()

        if (responseText.contains("\"message\":\"Not Found\"")) {
            return null
        }

        return json.decodeFromString(httpResponse.readText())
    }

    fun ApiClient.updateLimits(httpResponse: HttpResponse) {
        val resource =
            GithubResource(
                limit = httpResponse.headers["X-RateLimit-Limit"]?.toInt() ?: 0,
                remaining = httpResponse.headers["X-RateLimit-Limit"]?.toInt() ?: 0,
                reset = httpResponse.headers["X-RateLimit-Limit"]?.toLong() ?: 0,
                used = httpResponse.headers["X-RateLimit-Limit"]?.toInt() ?: 0,
            )

        val resourceType = httpResponse.headers["X-RateLimit-Resource"] ?: ""

        when (resourceType.lowercase()) {
            "core" -> resources[this] = resources[this]?.copy(core = resource)
            "search" -> resources[this] = resources[this]?.copy(search = resource)
            "graphql" -> resources[this] = resources[this]?.copy(graphql = resource)
            "integration_manifest" ->
                resources[this] = resources[this]?.copy(integrationManifest = resource)
        }
    }

    companion object {
        val CLIENTS: List<ApiClient> =
            listOf(
                    createGithubClient(
                        githubToken = GITHUB_TOKEN_1,
                        client = NordVpnClient.FIRST_CLIENT
                    ),
                    createGithubClient(
                        githubToken = GITHUB_TOKEN_2,
                        client = NordVpnClient.SECOND_CLIENT
                    )
                )
                .map { httpClient -> ApiClient(api = ApiImpl(httpClient), client = httpClient) }

        val client = ClientMonitor(CLIENTS)
        val resources: ConcurrentMap<ApiClient, Resources> = ConcurrentHashMap()
    }
}

@OptIn(ObsoleteCoroutinesApi::class)
class ClientMonitor(private val clients: List<ApiClient>) {

    suspend fun getClientBlocked(
        resources: Map<ApiClient, Resources>,
        githubResourceType: ResourceType
    ): ApiClient {
        val limits =
            clients.map { client -> client to resources[client].getByType(githubResourceType) }

        val firstClient =
            limits.firstOrNull { (_, resource) ->
                (resource?.remaining ?: 0) > 0 || resource?.remaining == DEFAULT_VALUE
            }

        if (firstClient != null) return firstClient.first

        val minDelayClient =
            limits.minByOrNull { (_, resource) -> resource?.reset ?: Long.MAX_VALUE }

        minDelayClient!!.second!!.reset.let { timeReset ->
            val delayTime = timeReset - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            delay(delayTime)
        }

        return minDelayClient.first
    }
}
