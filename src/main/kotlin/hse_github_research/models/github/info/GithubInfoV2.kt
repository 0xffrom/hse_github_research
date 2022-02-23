package hse_github_research.models.github

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GithubInfoV2(
    @SerialName("id") val id: Int? = null,
    @SerialName("login") val login: String,
    @SerialName("name") val name: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("bio") val bio: String? = null,
    @SerialName("blog") val blog: String? = null,
    @SerialName("company") val company: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("followers") val followers: Int? = null,
    @SerialName("following") val following: Int? = null,
    @SerialName("gravatar_id") val gravatarId: String? = null,
    @SerialName("location") val location: String? = null,
    @SerialName("public_gists") val publicGists: Int? = null,
    @SerialName("public_repos") val publicRepos: Int? = null,
    @SerialName("repos_url") val reposUrl: String? = null,
    @SerialName("site_admin") val siteAdmin: Boolean? = null,
    @SerialName("twitter_username") val twitterUsername: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("url") val url: String? = null
)

@Serializable
data class GithubUsersResponseV2(
    @SerialName("total_count") val totalCount: Int = 0,
    @SerialName("items") val users: List<GithubInfoV2> = emptyList()
)
