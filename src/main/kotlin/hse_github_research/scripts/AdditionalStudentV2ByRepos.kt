package hse_github_research.scripts

import hse_github_research.core.GithubProxyNetworkClient
import hse_github_research.models.StudentGeneralInfo
import hse_github_research.models.github.GithubInfoV2
import hse_github_research.models.github.ResourceType
import hse_github_research.models.github.repos.GithubReposV2
import hse_github_research.models.student.StudentV4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import kotlin.io.path.Path

private val singleNetworkSemaphore = Semaphore(1)

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    runBlocking {
        val oldStudents =
            with(withContext(Dispatchers.IO) { FileInputStream("data/students_v2.json") }) {
                Json.decodeFromStream<List<StudentV4>>(this)
            }

        var newStudents =
            oldStudents.map { student ->
                TempStudentV2(
                    githubInfo = student.githubInfo,
                    studentInfo = student.studentInfo,
                    followers = student.followers,
                )
            }

        val githubProxyNetworkClient = GithubProxyNetworkClient()

        var counter = 1
        oldStudents.forEach { oldStudent ->
            println(counter++)

            singleNetworkSemaphore.withPermit {
                val repos =
                    githubProxyNetworkClient.response<List<GithubReposV2>>(
                        ResourceType.CORE,
                    ) { getGithubRepos(oldStudent.githubInfo.login) }

                val replacedStudents =
                    newStudents.map {
                        if (it.githubInfo.login == oldStudent.githubInfo.login) {
                            TempStudentV2(
                                githubInfo = it.githubInfo,
                                studentInfo = it.studentInfo,
                                followers = it.followers,
                                repos = repos,
                            )
                        } else {
                            it
                        }
                    }

                newStudents = replacedStudents

                Files.deleteIfExists(Path("data/students_v3.json"))
                Json.encodeToStream(replacedStudents, FileOutputStream("data/students_v3.json"))
            }
        }
    }
}

@Serializable
data class TempStudentV2(
    val githubInfo: GithubInfoV2,
    val studentInfo: StudentGeneralInfo,
    val followers: List<GithubInfoV2>,
    val repos: List<GithubReposV2>? = null,
)
