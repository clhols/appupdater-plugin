package dk.youtec.appupdater

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.Input
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

    @Input
    val versionCode = dateFormat("yyMMddHH").format(Date()).toInt()

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun action() {
        outputFile.get().asFile.writeText("$versionCode")
        logger.log(LogLevel.INFO, "Generated a version code $versionCode")
    }
}