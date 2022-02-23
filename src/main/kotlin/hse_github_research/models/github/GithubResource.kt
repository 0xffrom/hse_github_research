package hse_github_research.models.github

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class ResourceType {
    SEARCH,
    CORE,
    GRAPHQL,
    INTEGRATION_MANIFEST,
    UNKNOWN,
}

@Serializable
data class GithubResource(
    @SerialName("limit") val limit: Int = DEFAULT_VALUE,
    @SerialName("remaining") val remaining: Int = DEFAULT_VALUE,
    @SerialName("reset") val reset: Long = DEFAULT_VALUE.toLong(),
    @SerialName("used") val used: Int = DEFAULT_VALUE,
) {
    companion object {
        const val DEFAULT_VALUE = -1
    }
}
