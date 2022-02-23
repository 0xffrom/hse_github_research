package hse_github_research.models.student

import hse_github_research.models.github.GithubInfo
import hse_github_research.models.github.GithubInfoV2
import hse_github_research.models.github.GithubRepos
import hse_github_research.models.github.GithubReposV2
import kotlinx.serialization.Serializable

@Serializable
data class StudentV1(
    val studentInfo: StudentGeneralInfo,
    val githubUser: GithubInfo,
)

@Serializable
data class StudentV2(
    val studentInfo: StudentGeneralInfo,
    val githubInfo: GithubInfo,
    val followers: List<GithubInfo>,
)

@Serializable
data class StudentV3(
    val studentInfo: StudentGeneralInfo,
    val githubInfo: GithubInfo,
    val followers: List<GithubInfo>,
    val repos: List<GithubRepos> = emptyList(),
)

@Serializable
data class StudentV4(
    val studentInfo: StudentGeneralInfo,
    val githubInfo: GithubInfoV2,
    val followers: List<GithubInfoV2>,
    val repositories: List<GithubReposV2>,
)
