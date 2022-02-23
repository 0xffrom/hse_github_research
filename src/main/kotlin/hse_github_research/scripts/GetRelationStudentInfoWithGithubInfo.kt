package hse_github_research.scripts

import hse_github_research.core.ext.parseJsonData
import hse_github_research.core.ext.saveJsonData
import hse_github_research.models.student.StudentV4
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val students = parseJsonData<List<StudentV4>>("students_v4_migrated_unique.json")

        val relations =
            students.sortedBy { studentV4 -> studentV4.studentInfo.email }.associate { student ->
                student.studentInfo.email to student.githubInfo.id
            }

        saveJsonData("student_info-to-github_id-relations", relations)
    }
}
