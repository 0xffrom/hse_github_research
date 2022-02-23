package hse_github_research.scripts.unused

import hse_github_research.models.github.GithubInfo
import hse_github_research.models.student.OldStudent
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.time.LocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream

private const val SERIALIZER_DATA_FOLDER = "data/serializer/"

enum class SerializeType {
    SCV,
    JSON,
}

suspend fun List<OldStudent>.serialize(serializeType: SerializeType, fileName: String) {
    val localTime = LocalDateTime.now().toString()

    when (serializeType) {
        SerializeType.SCV ->
            serializeToCsv(
                fileName = "$SERIALIZER_DATA_FOLDER/$localTime/$fileName-with-github.csv"
            )
        SerializeType.JSON -> {
            serializeToJson(fileName = "$SERIALIZER_DATA_FOLDER/$localTime/$fileName-full.json")

            filter { it.githubInfoList.isNotEmpty() }
                .serializeToJson(
                    fileName = "$SERIALIZER_DATA_FOLDER/$localTime/$fileName-with-github.json"
                )
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
private suspend fun List<OldStudent>.serializeToJson(fileName: String) {
    withContext(Dispatchers.IO) {
        val file = File(fileName)

        FileOutputStream(file)
    }
        .use { stream -> Json.encodeToStream(this, stream) }
}

private suspend fun List<OldStudent>.serializeToCsv(fileName: String) {
    withContext(Dispatchers.IO) {
        val file = File(fileName)

        FileWriter(file)
    }
        .use { fileWriter ->
            val githubInfoList = map { student -> student.githubInfoList }.flatten()

            githubInfoList.toCsvLine().map { line -> fileWriter.append(line) }
        }
}

fun List<GithubInfo>.toCsvLine(): List<String> =
    map { gitInfo ->
        listOf(
                gitInfo.id?.toString().orEmpty(),
                gitInfo.login,
                gitInfo.name.orEmpty(),
                gitInfo.avatarUrl.orEmpty(),
                gitInfo.bio.orEmpty(),
                gitInfo.blog.orEmpty(),
                gitInfo.company.orEmpty(),
                gitInfo.createdAt.orEmpty(),
                gitInfo.email.orEmpty(),
                gitInfo.eventsUrl.orEmpty(),
                gitInfo.followers?.toString().orEmpty(),
                gitInfo.followersUrl.orEmpty(),
                gitInfo.following?.toString().orEmpty(),
                gitInfo.gistsUrl.orEmpty(),
                gitInfo.gravatarId.orEmpty(),
                gitInfo.hireable?.toString().orEmpty(),
                gitInfo.htmlUrl.orEmpty(),
                gitInfo.location.orEmpty(),
                gitInfo.nodeId.orEmpty(),
                gitInfo.organizationsUrl.orEmpty(),
                gitInfo.siteAdmin?.toString().orEmpty(),
                gitInfo.twitterUsername.orEmpty()
            )
            .joinToString(separator = ",")
    }
        .let { list ->
            listOf(
                "id,login,name,avatarUrl,bio,blog,company,createdAt,email,eventsUrl,followers,followersUrl,following,gistsUrl,gravatarId,hireable,htmlUrl,location,nodeId,organizationsUrl,siteAdmin,twitterUsername"
            ) + list
        }
