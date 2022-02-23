package hse_github_research.scripts.unused

import hse_github_research.core.GithubProxyNetworkClient
import hse_github_research.models.github.GithubInfoV2
import hse_github_research.models.github.GithubUsersResponseV2
import hse_github_research.models.github.ResourceType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

// Поиск подписчиков у заданных юзеров
object GetGithubUsersByEmailsScript {

    private val singleSemaphore = Semaphore(1)
    private val githubClient = GithubProxyNetworkClient()

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun getGithubUsers(
        emails: List<String>,
        withTempData: Boolean = true,
        withSaveFile: Boolean = true,
        saveFileName: String = "students.json",
    ): List<GithubInfoV2> {
        val startTime = LocalDateTime.now().toKotlinLocalDateTime().toString()
        var iterationNumber = 0

        return emails
            .windowed(size = 30, step = 30)
            .flatMap { windowedEmails ->
                coroutineScope {
                    singleSemaphore.withPermit {
                        val results =
                            windowedEmails.mapNotNull { email ->
                                githubClient
                                    .response<GithubUsersResponseV2>(
                                        resourceType = ResourceType.SEARCH,
                                    ) { getGithubUsersByEmail(email) }
                                    ?.users
                                    ?.firstOrNull()
                                    ?: return@mapNotNull null
                            }

                        if (withTempData && results.isNotEmpty()) {
                            val file = File("temp/$startTime/gitusers$iterationNumber.json")
                            file.createNewFile()
                            FileOutputStream(File("temp/$startTime/gitusers$iterationNumber.json"))
                                .use { Json.encodeToStream(results, it) }
                        }

                        iterationNumber++
                        return@withPermit results
                    }
                }
            }
            .also { students ->
                if (withSaveFile) {
                    withContext(Dispatchers.IO) {
                        val file = File(saveFileName)

                        FileOutputStream(file)
                    }
                        .use { stream -> Json.encodeToStream(students, stream) }
                }
            }
    }
}
