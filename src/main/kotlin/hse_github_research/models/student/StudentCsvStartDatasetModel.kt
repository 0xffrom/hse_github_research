package hse_github_research.models.student

// Model for import from start dataset
data class StudentCsvStartDatasetModel(
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
)
