package hse_github_research.scripts

import hse_github_research.core.ext.parseJsonData
import hse_github_research.core.ext.saveJsonData
import hse_github_research.models.student.StudentV4
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val students = parseJsonData<List<StudentV4>>("students_v4_migrated_unique.json")
        val followers = students.flatMap { it.followers }

        val relations =
            students.associate { it.githubInfo.id to it.followers.map { follower -> follower.id } }

        val relationMap = mutableMapOf<Int, Int>()

        relations.forEach { (_, followers) ->
            followers.filterNot { followerId -> relations.keys.contains(followerId) }.forEach { followerId ->
                followerId?.let { relationMap[followerId] = (relationMap[followerId] ?: 0) + 1 }
            }
        }

        val counterMap = mutableMapOf<Int, Int>()

        relationMap.forEach { (_, count) -> counterMap[count] = (counterMap[count] ?: 0) + 1 }

        saveJsonData(
            "mostFollowers-counter",
            counterMap.toList().sortedByDescending { it.second }.associate { (x, y) -> x to y }
        )

        saveJsonData(
            "mostFollowers-ids",
            relationMap.toList().sortedByDescending { it.second }.associate { (x, y) -> x to y }
        )

        val emails =
            relationMap
                .toList()
                .associate { (id, followersCount) ->
                    followers.find { follower -> follower.id == id }!!.url to followersCount
                }
                .toList()
                .filter { it.second > 1 }
                .sortedByDescending { it.second }
                .associate { (x, y) -> x to y }

        saveJsonData("mostFollowers-emails-most-1-follower", emails)
    }
}
