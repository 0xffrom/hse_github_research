package hse_github_research.models.student

import hse_github_research.models.github.GithubInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Deprecated("Deprecated", ReplaceWith("StudentV2"))
@Serializable
data class OldStudent(
    @SerialName("general") val generalInfo: StudentGeneralInfo,
    @SerialName("github") val githubInfoList: List<GithubInfo> = emptyList()
)
