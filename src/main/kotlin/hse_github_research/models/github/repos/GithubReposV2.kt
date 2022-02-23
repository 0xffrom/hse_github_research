package hse_github_research.models.github.repos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubReposV2(
    @SerialName("id") val id: String? = null,
    @SerialName("language") val language: String? = null,
    @SerialName("archived") val archived: Boolean? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("disabled") val disabled: Boolean? = null,
    @SerialName("fork") val fork: Boolean? = null,
    @SerialName("forks_count") val forksCount: Int? = null,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("has_downloads") val hasDownloads: Boolean? = null,
    @SerialName("has_issues") val hasIssues: Boolean? = null,
    @SerialName("has_pages") val hasPages: Boolean? = null,
    @SerialName("has_projects") val hasProjects: Boolean? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("owner") val owner: Owner? = null,
    @SerialName("size") val size: Int? = null,
    @SerialName("stargazers_count") val stargazersCount: Int? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("visibility") val visibility: String? = null,
    @SerialName("watchers_count") val watchersCount: Int? = null,
) {

    @Serializable
    data class Owner(
        @SerialName("id") val id: Int?,
        @SerialName("login") val login: String?,
    )
}
