package dk.youtec.appupdater

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension
import java.util.Date

open class AppUpdaterPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project.tasks) {
            val generateProjectChangeLog = create("generateProjectChangeLog",
                    GenerateProjectChangeLogTask::class.java)

            create("tagProject", TagProjectTask::class.java).setMustRunAfter(listOf(
                    generateProjectChangeLog))

            whenTaskAdded {
                if (it.name.startsWith("generate")
                        && it.name.endsWith("ReleaseBuildConfig")) {
                    //println("Task name: ${task.name}")

                    it.dependsOn("tagProject")
                    it.dependsOn("generateProjectChangeLog")
                }
            }
        }

        val ext = project.extensions.getByName("ext") as ExtraPropertiesExtension

        //Equivalent to project.ext.getVersionCodeTimestamp = { -> }
        ext.set("getVersionCodeTimestamp", closure {
            if (project.hasProperty("devBuild")) {
                1
            } else {
                dateFormat("yyMMddHH00").format(Date()).toInt()
            }
        })
    }
}