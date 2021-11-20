package jiux.net.services

import com.intellij.openapi.project.Project
import jiux.net.RestfulToolkitBundle

class RestfulToolkitProjectService(project: Project) {

    init {
        println(RestfulToolkitBundle.message("projectService", project.name))
    }
}
