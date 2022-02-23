package hse_github_research.models.github.limit

import hse_github_research.models.github.GithubResource
import hse_github_research.models.github.ResourceType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Resources(
    @SerialName("code_scanning_upload") val codeScanningUpload: GithubResource = GithubResource(),
    @SerialName("core") val core: GithubResource = GithubResource(),
    @SerialName("graphql") val graphql: GithubResource = GithubResource(),
    @SerialName("integration_manifest") val integrationManifest: GithubResource = GithubResource(),
    @SerialName("search") val search: GithubResource = GithubResource()
)

fun Resources?.getByType(type: ResourceType) =
    this?.let {
        when (type) {
            ResourceType.CORE -> core
            ResourceType.INTEGRATION_MANIFEST -> integrationManifest
            ResourceType.SEARCH -> search
            ResourceType.GRAPHQL -> graphql
            ResourceType.UNKNOWN -> null
        }
    }
