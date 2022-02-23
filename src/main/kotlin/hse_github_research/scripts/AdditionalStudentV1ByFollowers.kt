package hse_github_research.scripts

import hse_github_research.core.GithubProxyNetworkClient
import hse_github_research.models.github.GithubInfo
import hse_github_research.models.github.ResourceType
import hse_github_research.models.student.StudentV1
import hse_github_research.models.student.StudentGeneralInfo
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import kotlin.io.path.Path
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream

private val singleNetworkSemaphore = Semaphore(1)

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    runBlocking {
        val oldStudents =
            with(
                withContext(Dispatchers.IO) {
                    FileInputStream("data/students_with_github_user.json")
                }
            ) { Json.decodeFromStream<List<StudentV1>>(this) }

        var newStudents =
            oldStudents
                .map { student ->
                    TempStudent(
                        githubInfo = student.githubUser,
                        studentInfo = student.studentInfo,
                    )
                }

        val githubProxyNetworkClient = GithubProxyNetworkClient()

        var counter = 1
        oldStudents.forEach { oldStudent ->
            println(counter++)

            singleNetworkSemaphore.withPermit {
                val followers =
                    githubProxyNetworkClient.response<List<GithubInfo>>(
                        ResourceType.CORE,
                    ) { getGithubFollowers(oldStudent.githubUser.login) }

                println("FOLLOWERS = $followers")

                val replacedStudents =
                    newStudents.map {
                        if (it.githubInfo.login == oldStudent.githubUser.login) {
                            TempStudent(
                                githubInfo = it.githubInfo,
                                studentInfo = it.studentInfo,
                                followers = followers.orEmpty(),
                            )
                        } else {
                            it
                        }
                    }

                newStudents = replacedStudents

                Files.delete(Path("data/students_v2.json"))
                Json.encodeToStream(replacedStudents, FileOutputStream("data/students_v2.json"))
            }
        }
    }
}

@Serializable
data class TempStudent(
    val githubInfo: GithubInfo,
    val studentInfo: StudentGeneralInfo,
    val followers: List<GithubInfo>? = null,
)
