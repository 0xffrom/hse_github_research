package hse_github_research.scripts.unused

import hse_github_research.core.GithubProxyNetworkClient
import hse_github_research.models.github.GithubInfoV2
import hse_github_research.models.github.ResourceType
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object FindGithubFollowersWithNearNeighborsScript {

    private val singleSemaphore = Semaphore(1)
    private val githubClient = GithubProxyNetworkClient()

    suspend fun findFollowersBy(startUserName: String, neighbors: Int): Set<GithubInfoV2> {
        val githubUsers = mutableSetOf<GithubInfoV2>()

        val startUser =
            githubClient.response<GithubInfoV2>(resourceType = ResourceType.CORE) {
                getGithubUser(startUserName)
            }
        githubUsers.add(startUser!!)

        // TODO: add timeout
        repeat(neighbors) {
            githubUsers.forEach { user -> githubUsers.addAll(githubUsers.addFollowersByUser(user)) }
        }

        return githubUsers
    }

    private suspend fun Set<GithubInfoV2>.addFollowersByUser(
        user: GithubInfoV2,
    ): Set<GithubInfoV2> {
        val newUsers = mutableSetOf<GithubInfoV2>()
        val followers =
            githubClient.response<List<GithubInfoV2>>(resourceType = ResourceType.CORE) {
                getGithubFollowers(user.login)
            }

        followers
            ?.filterNot { follower ->
                (this + newUsers).find { user -> user.login != follower.login } != null
            }
            ?.forEach { follower ->
                singleSemaphore.withPermit {
                    val userByFollow =
                        githubClient.response<GithubInfoV2>(resourceType = ResourceType.CORE) {
                            getGithubUser(follower.login)
                        }

                    newUsers.add(userByFollow!!)
                }
            }

        return newUsers
    }
}
