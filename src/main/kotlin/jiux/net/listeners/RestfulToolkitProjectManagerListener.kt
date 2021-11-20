package jiux.net.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import jiux.net.services.RestfulToolkitProjectService

internal class RestfulToolkitProjectManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        project.service<RestfulToolkitProjectService>()
    }
}
