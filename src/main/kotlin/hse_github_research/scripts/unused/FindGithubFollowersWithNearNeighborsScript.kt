package hse_github_research.scripts.unused

import hse_github_research.core.GithubProxyNetworkClient
import hse_github_research.models.github.GithubInfo
import hse_github_research.models.github.ResourceType
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

@OptIn(ExperimentalTime::class)
object FindGithubFollowersWithNearNeighborsScript {

    private val singleSemaphore = Semaphore(1)
    private val githubClient = GithubProxyNetworkClient()

    suspend fun findFollowersBy(startUserName: String, neighbors: Int): Set<GithubInfo> {
        val githubUsers = mutableSetOf<GithubInfo>()

        val startUser =
            githubClient.response<GithubInfo>(resourceType = ResourceType.CORE) { getGithubUser(startUserName) }
        githubUsers.add(startUser!!)

        // TODO: add timeout
        repeat(neighbors) {
            githubUsers.forEach { user -> githubUsers.addAll(githubUsers.addFollowersByUser(user)) }
        }

        return githubUsers
    }

    private suspend fun Set<GithubInfo>.addFollowersByUser(user: GithubInfo): Set<GithubInfo> {
        val newUsers = mutableSetOf<GithubInfo>()
        val followers =
            githubClient.response<List<GithubInfo>>(resourceType = ResourceType.CORE) {
                getGithubFollowers(user.login)
            }

        followers
            ?.filterNot { follower ->
                (this + newUsers).find { user -> user.login != follower.login } != null
            }
            ?.forEach { follower ->
                singleSemaphore.withPermit {
                    val userByFollow =
                        githubClient.response<GithubInfo>(resourceType = ResourceType.CORE) {
                            getGithubUser(follower.login)
                        }

                    newUsers.add(userByFollow!!)
                }
            }

        return newUsers
    }
}
