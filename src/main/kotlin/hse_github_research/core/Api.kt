import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

interface Api {

    suspend fun getGithubUser(userName: String): HttpResponse
    suspend fun getGithubFollowers(userName: String): HttpResponse
    suspend fun getGithubRepos(userName: String): HttpResponse
    suspend fun getGithubUsersByEmail(email: String): HttpResponse
    suspend fun getGithubLimit(): HttpResponse

    /**
     *     suspend fun getGithubUser(userName: String) : GithubInfo
    suspend fun getGithubFollowers(userName: String) : List<GithubInfo>
    suspend fun getGithubUsersByEmail(email: String) : GithubUsersResponse
    suspend fun getGithubLimit() : GithubResponseLimit

    }
     */
}

class ApiImpl(private val client: HttpClient) : Api {

    override suspend fun getGithubUser(userName: String): HttpResponse {
        return client.get("users/$userName")
    }

    override suspend fun getGithubFollowers(userName: String): HttpResponse {
        return client.get("users/$userName/followers")
    }

    override suspend fun getGithubRepos(userName: String): HttpResponse {
        return client.get("users/$userName/repos")
    }

    override suspend fun getGithubUsersByEmail(email: String): HttpResponse {
        return client.get("search/users?q=$email")
    }

    override suspend fun getGithubLimit(): HttpResponse {
        return client.get("rate_limit")
    }
}
