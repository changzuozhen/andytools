import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern


val testfile = "out/test.txt"
var handlePath = ""
val fileTypes = arrayOf(
    "mp3",
    "m4a",
    "mp4",
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
    "mp3",
    "doc",
    "doc",
    "doc",
    "doc",
    "doc",
    "doc"
)

fun main(args: Array<String>) {

    if (args == null || args.isEmpty()) {
        println("使用测试模式，只扫描，并输出结果，并不造成实际改动")
    }
    if (args.isNotEmpty()) {
        if (args.contains("confirmed")) {
            println("将造成实际改动")
//            DEBUG = false
        }
    }
    var file = File("")
    println("将使用当前目录扫描")
    println(file.absoluteFile)
    handlePath = file.absolutePath
    init()
//    if (args == null || args.size == 0) {
//        writeLog("请在命令后面带上需要处理的完整路径")
//    }
//    args.forEach {
//        writeLog(it)
//        handlePath(args[0])
//    }

    handlePathWithHandler(handlePath) { path, file ->
        refactorFile(path, file)
    }
//    test1()
}

fun init() {
    val logFile = File(logPath)
    if (logFile.exists()) {
        logFile.writeText("")
    }
}

fun testMake() {
    val testf = File(testfile)
    var lines = testf.readLines()
    lines.map { s: String -> File(s) }
        .forEach { file: File ->
            if (fileTypes.contains(file.extension)) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
        }
}

fun test1() {
    val testf = File(testfile)
    var lines = testf.readLines()
    lines.map { s: String -> File(s) }
        .forEach { file: File ->
            refactorFile(testfile, file)
        }
}

private fun refactorFile(path: String, file: File) {
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
        val summeryType = fileTypeSummerys[indexOf]
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
