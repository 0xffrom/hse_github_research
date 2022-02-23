package hse_github_research.scripts.unused

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val generalInfoList = ParseScvStartDatasetScript.parse()
    val emails = generalInfoList.map { it.email }

    val githubInfoList = GetGithubUsersByEmailsScript.getGithubUsers(emails = emails)

    val students =
        MergeGithubInfoAndGeneralInfoScript.merge(
            githubInfoList = githubInfoList,
            generalInfoList = generalInfoList
        )

    students.serialize(SerializeType.SCV, "first-iteration")
    students.serialize(SerializeType.JSON, "first-iteration")
}
