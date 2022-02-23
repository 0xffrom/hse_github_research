package hse_github_research.models.student

import hse_github_research.models.StudentGeneralInfo
import hse_github_research.models.github.GithubInfoV2
import hse_github_research.models.github.repos.GithubReposV2
import kotlinx.serialization.Serializable

@Serializable
data class StudentV4(
    val studentInfo: StudentGeneralInfo,
    val githubInfo: GithubInfoV2,
    val followers: List<GithubInfoV2>,
    val repositories: List<GithubReposV2>,
)