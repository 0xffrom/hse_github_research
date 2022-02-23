package hse_github_research.scripts

import hse_github_research.core.ext.parseJsonData
import hse_github_research.core.ext.saveJsonData
import hse_github_research.models.student.StudentV4
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val students =
            parseJsonData<List<StudentV4>>("students_v4_migrated.json")
                .map {
                    it.copy(
                        studentInfo =
                        it.studentInfo.copy(
                            email = it.studentInfo.email.trimIndent().trim('\"').lowercase()
                        )
                    )
                }
                .sortedBy { studentV4 -> studentV4.studentInfo.email }
                .distinctBy { it.githubInfo.id }


        saveJsonData("students_v4_migrated_unique", students)
    }
}
