package hse_github_research.scripts.unused

import hse_github_research.models.github.GithubInfo
import hse_github_research.models.student.OldStudent
import hse_github_research.models.student.StudentGeneralInfo

object MergeGithubInfoAndGeneralInfoScript {

    fun merge(
        githubInfoList: List<GithubInfo>,
        generalInfoList: List<StudentGeneralInfo>
    ): List<OldStudent> {
        return generalInfoList.map { generalInfo ->
            OldStudent(
                generalInfo = generalInfo,
                githubInfoList =
                    githubInfoList.filter { githubInfo -> generalInfo.email == githubInfo.email }
            )
        }
    }
}
