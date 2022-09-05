package online.flowerinsnow.flowerairadar.kook

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.net.URL
import java.nio.charset.StandardCharsets

class Launcher

val ucp : Field = Class.forName("jdk.internal.loader.BuiltinClassLoader").getDeclaredField("ucp")
val addURL: Method = ucp.type.getDeclaredMethod("addURL", URL::class.java)

suspend fun main() {
    addURL.isAccessible = true
    ucp.isAccessible = true

    println("Loading libraries, please wait...")
    val `in` = Launcher::class.java.getResourceAsStream("/libraries.txt")!!
    val isr = InputStreamReader(`in`, StandardCharsets.UTF_8)
    val br = BufferedReader(isr)
    var line: String?
    while (true) {
        line = withContext(Dispatchers.IO) {
            br.readLine()
        }
        if (line == null) {
            break
        }
        if (line.startsWith("#")) {
            continue
        }
        addToURL(File(line))
    }

    Main.start()
}

private fun addToURL(file : File) {
    when {
        file.isFile -> {
            // Launcher.class.getClassLoader.ucp.addURL(URL)
            addURL.invoke(ucp.get(Launcher::class.java.classLoader), file.toURI().toURL())
        }
        file.isDirectory -> {
            file.listFiles { pathname ->
                pathname.isDirectory || (pathname.isFile && pathname.name.endsWith(".jar", true))
            }?.forEach {
                addToURL(it)
            }
        }
        else -> {
            System.err.println("No such file or directory: ${file.path}")
        }
    }
}