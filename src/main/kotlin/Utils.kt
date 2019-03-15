import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

var DEBUG = true
const val logPath = "changelog.txt"
const val FILENAME = logPath

fun handlePathWithHandler(path: String, handler: (String, File) -> Unit) {
    val dir = File(path)
    if (!dir.exists()) {
        print("路径不存在：$path")
        return
    }
    val dirs = ArrayList<File>()
    traversePath(dir, dirs)
    dirs.forEach { it: File ->
        handler(path, it)
    }

    delNullDir(dir)
}


fun traversePath(dir: File, files: ArrayList<File>) {
    if (dir.exists()) {
        if (dir.isDirectory) {
            dir.listFiles().forEach { dir2 ->
                traversePath(dir2, files)
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

fun renameFile(destFile: File, fromFile: File) {
    if (destFile.equals(fromFile)) {
        log("samePath:$destFile")
        return
    }
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
