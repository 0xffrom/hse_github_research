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
    val contacts: List<EmailContact>
) : Serializable

@kotlinx.serialization.Serializable
data class EmailContact(
    val email: String,
    val emailType: String,
) : Serializable

fun List<StudentCsvStartDatasetModel>.toStudentGeneralInfoList(): List<StudentGeneralInfo> {
    // Сгруппируем по фио, чтобы выполнить нормализацию
    return groupBy(StudentCsvStartDatasetModel::fio).map { (_, models) ->
        val model = models.first()

        StudentGeneralInfo(
            fio = model.fio,
            firstName = model.firstName,
            secondName = model.secondName,
            thirdName = model.thirdName,
            faculty = model.faculty,
            level = model.level,
            educationProgram = model.educationProgram,
            numberCourse = model.numberCourse,
            contacts =
                models.map { contactModel ->
                    EmailContact(email = contactModel.email, emailType = contactModel.contactType)
                }
        )
    }
}
