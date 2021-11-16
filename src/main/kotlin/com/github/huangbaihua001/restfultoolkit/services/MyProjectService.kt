package com.github.huangbaihua001.restfultoolkit.services

import com.intellij.openapi.project.Project
import com.github.huangbaihua001.restfultoolkit.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
