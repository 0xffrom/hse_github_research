package hse_github_research.models.student

import hse_github_research.models.github.GithubInfo
import kotlinx.serialization.Serializable

@Serializable data class Student(val generalInfo: StudentGeneralInfo, val githubInfo: GithubInfo)
