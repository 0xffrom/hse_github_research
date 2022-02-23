package hse_github_research.models.student

import java.io.Serializable

@kotlinx.serialization.Serializable
data class StudentGeneralInfo(
    val fio: String,
    val firstName: String,
    val secondName: String,
    val thirdName: String,
    val faculty: String,
    val level: String,
    val educationProgram: String,
    val numberCourse: String,
    val email: String,
    val contactType: String,
) : Serializable