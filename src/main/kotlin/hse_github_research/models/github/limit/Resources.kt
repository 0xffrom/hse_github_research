package hse_github_research.models.github.limit

import hse_github_research.models.github.GithubSearchResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Resources(
    @SerialName("code_scanning_upload") val codeScanningUpload: CodeScanningUpload,
    @SerialName("core") val core: Core,
    @SerialName("graphql") val graphql: Graphql,
    @SerialName("integration_manifest") val integrationManifest: IntegrationManifest,
    @SerialName("search") val search: GithubSearchResponse
)
