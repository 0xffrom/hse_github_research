package hse_github_research.models.student

import hse_github_research.models.StudentGeneralInfo
import hse_github_research.models.github.GithubInfoV2
import kotlinx.serialization.Serializable

@Deprecated("")
@Serializable
data class StudentV2(
    val studentInfo: StudentGeneralInfo,
    val githubInfo: GithubInfoV2,
    val followers: List<GithubInfoV2>,
)