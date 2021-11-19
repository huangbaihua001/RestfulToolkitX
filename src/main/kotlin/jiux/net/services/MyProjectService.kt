package jiux.net.services

import com.intellij.openapi.project.Project
import jiux.net.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
