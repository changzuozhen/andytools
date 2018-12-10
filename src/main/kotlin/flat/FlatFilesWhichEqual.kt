package flat

import DEBUG
import fileTypes
import handlePathWithHandler
import log
import logPath
import renameFile
import java.io.File

const val handlePath = "out/"
fun main(args: Array<String>) {
    val flatFilesWhichEqual = FlatFilesWhichEqual()
    flatFilesWhichEqual.init()
    handlePathWithHandler(handlePath) { path, file ->
        flatFilesWhichEqual.refactorFile(path, file)
    }
}

class FlatFilesWhichEqual {

    fun init() {
        val logFile = File(logPath)
        if (logFile.exists()) {
            logFile.writeText("")
        }
    }

    fun refactorFile(path: String, file: File) {
        val line = file.absolutePath
        if (file.name.startsWith("._") || ".DS_Store".equals(file.name)) {
            if (DEBUG) {
                log("shouldDelete:" + file.absolutePath)
            } else {
                log("shouldDelete:" + file.absolutePath)
                file.delete()
            }
            return
        }
        if (DEBUG || file.isFile) {
            val indexOf = fileTypes.indexOf(file.extension.toLowerCase())
            if (indexOf == -1) {
                log("error:$file")
                return
            }

            if (file.nameWithoutExtension.contains(file.parentFile.name)) {
                val destFile = File("${file.parentFile.parentFile}/${file.name}")
                if (DEBUG) {
                    log("$file -> $destFile")
                } else {
                    renameFile(destFile, file)
                }
            }

//            val destFile = File("$path/${file.name}")
//            if (DEBUG) {
//                log("$file -> $destFile")
//            } else {
//                renameFile(destFile, file)
//            }
        }
    }
}

