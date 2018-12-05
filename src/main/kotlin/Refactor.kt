import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.regex.Matcher
import java.util.regex.Pattern


var DEBUG = true
const val logPath = "out/a.txt"
const val FILENAME = logPath
const val handlePath = "out/"
val testfile = ""

val fileTypes = arrayOf(
    "mp3",
    "m4a",
    "pdf",
    "png",
    "jpg",
    "txt",
    "doc",
    "docx"
)

val fileTypeSummerys = arrayOf(
    "mp3",
    "mp3",
    "doc",
    "doc",
    "doc",
    "doc",
    "doc",
    "doc"
)

fun main(args: Array<String>) {
    init()
//    if (args == null || args.size == 0) {
//        writeLog("请在命令后面带上需要处理的完整路径")
//    }
//    args.forEach {
//        writeLog(it)
//        handlePath(args[0])
//    }

    handlePath(handlePath)
//    test1()
}

fun init() {
    val logFile = File(logPath)
    if (logFile.exists()) {
        logFile.writeText("")
    }
}

fun handlePath(path: String) {
    val dir = File(path)
    if (!dir.exists()) {
        print("路径不存在：$path")
        return
    }
    val dirs = ArrayList<File>()
    triversePath(dir, dirs)
    dirs.forEach {
        refactorFile(it)
    }

    delNullDir(dir)
}

private fun test1() {
    val file = File(testfile)
    var lines = file.readLines()
    lines.map { s: String -> File(s) }
        .forEach { file: File ->
            refactorFile(file)
        }
}

private fun refactorFile(file: File) {
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
        val summeryType = fileTypeSummerys[fileTypes.indexOf(file.extension.toLowerCase())]
        val matcher1 = Pattern.compile("/(\\d{4})年/").matcher(line)
        if (handle(matcher1, line, summeryType, file)) {
            return
        }

        val matcher2 = Pattern.compile("/(\\d{4})年(\\d{2}|\\d{1})月/").matcher(line)
        if (handle(matcher2, line, summeryType, file)) {
            return
        }
    }
}

private fun handle(matcher: Matcher, line: String, summeryType: String, file: File): Boolean {
    if (matcher.find()) {
        val matchResult = matcher.toMatchResult()
        val dest1 = line.subSequence(
            0,
            matchResult.start()
        ).toString() + "/" + matchResult.group(1) + "年${summeryType}/"
        var dest2 = line.subSequence(matchResult.end(), line.length).toString()
            .replace("/", "_")
        val destFile = File("$dest1/$dest2")
        if (!file.equals(destFile)) {
            if (DEBUG) {
                log("$file -> $destFile")
            } else {
                renameFile(destFile, file)
            }
        }
        return true
    }
    return false
}

private fun renameFile(destFile: File, fromFile: File) {
    if (!destFile.parentFile.exists()) {
        destFile.parentFile.mkdirs()
    }
    if (fromFile.exists()) {
        log("$fromFile -> $destFile")
        fromFile.renameTo(destFile)
        return
    }
//    writeLog("ignore $fromFile")
}

fun triversePath(dir: File, files: ArrayList<File>) {
    if (dir.exists()) {
        if (dir.isDirectory) {
            dir.listFiles().forEach { dir2 ->
                triversePath(dir2, files)
            }
        } else {
            files.add(dir)
        }
    }
}

fun delNullDir(f: File) {
    for (f1 in f.listFiles()) {
        if (f1.isDirectory) {
            delNullDir(f1)
            //一直递归到最后的目录
            if (f1.listFiles().isEmpty()) {
                //如果是文件夹里面没有文件证明是空文件，进行删除
                if (DEBUG) {
                    log("delete:$f1")
                } else {
                    log("delete:$f1")
                    f1.delete()
                }
            }
        }
    }
}


fun log(info: String) {
//    if (DEBUG) {
//        println(info)
//    } else {
    var bw: BufferedWriter? = null
    var fw: FileWriter? = null
    try {
        fw = FileWriter(FILENAME, true)
        bw = BufferedWriter(fw)
        bw.write(info)
        bw.write("\n")
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            bw?.close()
            fw?.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }
    println(info)
//    }
}
