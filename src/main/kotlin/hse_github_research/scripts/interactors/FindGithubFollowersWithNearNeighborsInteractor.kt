package hse_github_research.scripts.interactors

import getGithubFollowers
import getGithubUser
import hse_github_research.core.NetworkClient
import hse_github_research.models.github.GithubInfo
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

@OptIn(ExperimentalTime::class)
object FindGithubFollowersWithNearNeighborsInteractor {

    private val singleSemaphore = Semaphore(1)
    private val githubClient = NetworkClient.githubClient

    suspend fun findFollowersBy(startUserName: String, neighbors: Int): Set<GithubInfo> {
        val githubUsers = mutableSetOf<GithubInfo>()

        githubUsers.add(githubClient.getGithubUser(userName = startUserName))

        // TODO: add timeout
        repeat(neighbors) {
            githubUsers.forEach { user -> githubUsers.addAll(githubUsers.addFollowersByUser(user)) }
        }

        return githubUsers
    }

    private suspend fun Set<GithubInfo>.addFollowersByUser(user: GithubInfo): Set<GithubInfo> {
        val newUsers = mutableSetOf<GithubInfo>()
        val followers = githubClient.getGithubFollowers(user.login)

        followers
            .filterNot { follower ->
                (this + newUsers).find { user -> user.login != follower.login } != null
            }
            .forEach { follower ->
                singleSemaphore.withPermit {
                    val userByFollow = githubClient.getGithubUser(follower.login)

                    newUsers.add(userByFollow)
                }
            }

        return newUsers
    }
}
