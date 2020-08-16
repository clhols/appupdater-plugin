package dk.youtec.appupdater

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.util.*

/**
 * Generates Project change log since last tag
 */
abstract class VersionCodeTask : AppUpdaterGroupTask() {

    init {
        description = "Generates a version code by timestamp"
    }

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun action() {
        val versionCode = dateFormat("yyMMddHHmm").format(Date()).toInt()
        outputFile.get().asFile.writeText("$versionCode")
    }
}