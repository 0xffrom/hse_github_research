package hse_github_research.scripts.interactors

import getGithubUsersByEmail
import hse_github_research.core.NetworkClient
import hse_github_research.models.github.GithubInfo
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream

private const val GITHUB_TIMEOUT = 60_000L

// Поиск подписчиков у заданных юзеров
object GetGithubUsersByEmailsInteractor {

    private val singleSemaphore = Semaphore(1)
    private val githubClient = NetworkClient.githubClient

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun getGithubUsers(
        emails: List<String>,
        withTempData: Boolean = true,
        withSaveFile: Boolean = true,
        saveFileName: String = "students.json"
    ): List<GithubInfo> {
        val startTime = LocalDateTime.now().toKotlinLocalDateTime().toString()
        var iterationNumber = 0

        return emails
            .windowed(size = 30, step = 30)
            .map { windowedEmails ->
                coroutineScope {
                    singleSemaphore.withPermit {
                        val results =
                            windowedEmails.mapNotNull { email ->
                                githubClient.getGithubUsersByEmail(email).users.firstOrNull()
                                    ?: return@mapNotNull null
                            }

                        delay(GITHUB_TIMEOUT)

                        if (withTempData && results.isNotEmpty()) {
                            FileOutputStream(File("temp/$startTime/followers$iterationNumber.json"))
                                .use { Json.encodeToStream(results, it) }
                        }

                        iterationNumber++
                        return@withPermit results
                    }
                }
            }
            .flatten()
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
