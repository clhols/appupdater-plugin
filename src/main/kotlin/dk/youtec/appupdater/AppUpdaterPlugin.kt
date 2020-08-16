package dk.youtec.appupdater

import com.android.build.api.variant.VariantOutputConfiguration.OutputType
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension

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

        project.plugins.withType(AppPlugin::class.java) {
            val extension = project.extensions.getByName("android") as BaseAppModuleExtension
            extension.configure(project)
        }
    }
}

fun BaseAppModuleExtension.configure(project: Project) {
    // onVariantProperties registers an action that configures variant properties during
    // variant computation (which happens during afterEvaluate)
    onVariantProperties {
        // applies to all variants. This excludes test components (unit test and androidTest)
    }

    // use filter to apply onVariantProperties to a subset of the variants
    onVariantProperties.withBuildType("release") {
        // Because app module can have multiple output when using mutli-APK, versionCode/Name
        // are only available on the variant output.
        // Here gather the output when we are in single mode (ie no multi-apk)
        val mainOutput = this.outputs.single { it.outputType == OutputType.SINGLE }

        // create version Code generating task
        val versionCodeTask = project.tasks.register("computeVersionCodeFor${name}", VersionCodeTask::class.java) {
            it.outputFile.set(project.layout.buildDirectory.file("versionCode.txt"))
        }

        // wire version code from the task output
        // map will create a lazy Provider that
        // 1. runs just before the consumer(s), ensuring that the producer (VersionCodeTask) has run
        //    and therefore the file is created.
        // 2. contains task dependency information so that the consumer(s) run after the producer.
        mainOutput.versionCode.set(versionCodeTask.map { it.outputFile.get().asFile.readText().toInt() })
    }

    onVariantProperties.withBuildType("debug") {
        val mainOutput = this.outputs.single { it.outputType == OutputType.SINGLE }
        mainOutput.versionCode.set(1)
    }
}