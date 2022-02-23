package hse_github_research.models.student

import hse_github_research.models.StudentGeneralInfo
import hse_github_research.models.github.GithubInfoV2
import kotlinx.serialization.Serializable

@Deprecated("")
@Serializable
data class StudentV1(
    val studentInfo: StudentGeneralInfo,
    val githubUser: GithubInfoV2,
)
