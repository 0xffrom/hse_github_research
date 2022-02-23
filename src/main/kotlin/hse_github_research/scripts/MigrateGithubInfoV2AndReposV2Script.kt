package hse_github_research.scripts

import hse_github_research.models.github.GithubInfo
import hse_github_research.models.github.GithubInfoV2
import hse_github_research.models.github.GithubRepos
import hse_github_research.models.github.GithubReposV2
import hse_github_research.models.student.StudentV3
import hse_github_research.models.student.StudentV4
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    runBlocking {
        val oldStudents =
            Json.decodeFromStream<List<StudentV3>>(
                withContext(Dispatchers.IO) { FileInputStream("data/students_v3.json") }
            )

        val newStudents = oldStudents
            .map { student ->
                StudentV4(
                    studentInfo = student.studentInfo,
                    repositories = student.repos.map { reposV1 -> reposV1.toV2() },
                    githubInfo = student.githubInfo.toV2(),
                    followers = student.followers.map { followerV1 -> followerV1.toV2() }
                )
            }
            .sortedBy { student -> student.studentInfo.firstName }

        Json.encodeToStream(
            newStudents,
            withContext(Dispatchers.IO) { FileOutputStream("data/students_v4_migrated.json") }
        )
    }
}

private fun GithubInfo.toV2() =
    GithubInfoV2(
        id = id,
        login = login,
        name = name,
        avatarUrl = avatarUrl,
        bio = bio,
        blog = blog,
        company = company,
        createdAt = createdAt,
        email = email,
        followers = followers,
        following = following,
        gravatarId = gravatarId,
        location = location,
        publicGists = publicGists,
        publicRepos = publicRepos,
        reposUrl = reposUrl,
        siteAdmin = siteAdmin,
        twitterUsername = twitterUsername,
        type = type,
        url = url
    )

private fun GithubRepos.toV2() =
    GithubReposV2(
        id = id,
        language = language,
        archived = archived,
        description = description,
        disabled = disabled,
        fork = fork,
        forksCount = forksCount,
        fullName = fullName,
        hasDownloads = hasDownloads,
        hasIssues = hasIssues,
        hasPages = hasPages,
        hasProjects = hasProjects,
        name = name,
        size = size,
        stargazersCount = stargazersCount,
        url = url,
        visibility = visibility,
        watchersCount = watchersCount,
        owner = GithubReposV2.Owner(id = owner?.id, login = owner?.login)
    )
