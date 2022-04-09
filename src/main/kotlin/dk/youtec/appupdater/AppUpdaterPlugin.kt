package dk.youtec.appupdater

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.VariantOutputConfiguration.OutputType
import com.android.build.gradle.AppPlugin
import org.gradle.kotlin.dsl.withType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType

open class AppUpdaterPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project.tasks) {
            val generateProjectChangeLog = create<GenerateProjectChangeLogTask>("generateProjectChangeLog")

            create<TagProjectTask>("tagProject").setMustRunAfter(
                listOf(
                    generateProjectChangeLog
                )
            )

            whenTaskAdded {
                if (name.startsWith("generate")
                    && name.endsWith("ReleaseBuildConfig")
                ) {
                    //println("Task name: ${task.name}")

                    dependsOn("tagProject")
                    dependsOn("generateProjectChangeLog")
                }
            }
        }

        project.plugins.withType<AppPlugin> {
            val extension = project.extensions.getByType<ApplicationAndroidComponentsExtension>()
            extension.configure(project)
        }
    }
}

fun ApplicationAndroidComponentsExtension.configure(project: Project) {
    onVariants { variant ->
        if (variant.buildType == "release") {

            // create version Code generating task
            val versionCodeTask = project.tasks.register("computeVersionCodeFor${variant.name}", VersionCodeTask::class.java) {
                outputFile.set(project.layout.buildDirectory.file("versionCode.txt"))
            }

            // Because app module can have multiple output when using multi-APK, versionCode/Name
            // are only available on the variant output.
            // Here gather the output when we are in single mode (ie no multi-apk)
            val mainOutput = variant.outputs.single { it.outputType == OutputType.SINGLE }

            // wire version code from the task output
            // map will create a lazy Provider that
            // 1. runs just before the consumer(s), ensuring that the producer (VersionCodeTask) has run
            //    and therefore the file is created.
            // 2. contains task dependency information so that the consumer(s) run after the producer.
            mainOutput.versionCode.set(versionCodeTask.map { it.outputFile.get().asFile.readText().toInt() })
        } else if (variant.buildType == "debug") {
            val mainOutput = variant.outputs.single { it.outputType == OutputType.SINGLE }
            mainOutput.versionCode.set(1)
        }
    }
}