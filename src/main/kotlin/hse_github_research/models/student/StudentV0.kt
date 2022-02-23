package hse_github_research.models.student

import hse_github_research.models.StudentGeneralInfo
import hse_github_research.models.github.GithubInfoV2
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Deprecated("")
@Serializable
data class StudentV0(
    @SerialName("general") val generalInfo: StudentGeneralInfo,
    @SerialName("github") val githubInfoList: List<GithubInfoV2> = emptyList(),
)
