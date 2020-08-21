package dk.youtec.appupdater

import com.android.build.gradle.BasePlugin

/**
 * Task to tag project with version code
 */
open class TagProjectTask : AppUpdaterGroupTask() {

    init {
        description = "Tag project in Git with version code"
    }

    @org.gradle.api.tasks.TaskAction
    fun action() {

        androidBasePlugin
                ?: throw org.gradle.api.GradleException("You must apply the Android plugin or the Android library plugin before using this plugin")

        val versionCode = project.layout.buildDirectory.file("versionCode.txt").get().asFile.readText().toInt()

        val tagResult = "git tag $versionCode".execute()
        if (tagResult.trim().isNotEmpty())
            throw org.gradle.api.GradleException("Error tagging project with $versionCode: $tagResult")

        val pushResult = "git push origin $versionCode".execute()
        if (pushResult.trim().isNotEmpty())
            throw org.gradle.api.GradleException("Error pushing tag: $pushResult")
    }

    private val androidBasePlugin: BasePlugin?
        get() = (project.plugins.findPlugin("com.android.application")
                ?: project.plugins.findPlugin("com.android.library")) as BasePlugin?
}