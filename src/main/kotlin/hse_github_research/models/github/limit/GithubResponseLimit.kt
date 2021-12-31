package hse_github_research.models.github.limit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubResponseLimit(
    @SerialName("rate") val rate: Rate,
    @SerialName("resources") val resources: Resources
)
