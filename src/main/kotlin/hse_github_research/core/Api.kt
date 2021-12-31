import hse_github_research.models.github.GithubInfo
import hse_github_research.models.github.GithubUsersResponse
import hse_github_research.models.github.limit.GithubResponseLimit
import io.ktor.client.*
import io.ktor.client.request.*

suspend fun HttpClient.getGithubUser(userName: String) = get<GithubInfo>("users/$userName")

suspend fun HttpClient.getGithubFollowers(userName: String) =
    get<List<GithubInfo>>("users/$userName/followers")

suspend fun HttpClient.getGithubUsersByEmail(email: String) =
    get<GithubUsersResponse>("search/users?q=$email")

suspend fun HttpClient.getGithubLimit() = get<GithubResponseLimit>("rate_limit")
