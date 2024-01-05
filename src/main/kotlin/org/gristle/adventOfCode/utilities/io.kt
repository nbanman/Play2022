@file:Suppress("unused")

package org.gristle.adventOfCode.utilities

import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate

/**
 * Reads lines from the given input txt file.
 */
fun readInput(subpackage: String = "", name: String = "input") =
    File("src/main/kotlin/org/gristle/adventOfCode/$subpackage", "$name.txt").readLines()

/**
 * Reads input txt file to string
 */
fun readRawInput(subpackage: String = "", name: String = "input") =
    File("src/main/kotlin/org/gristle/adventOfCode/$subpackage", "$name.txt").readText()

/**
 * Reads input txt file to string
 */
fun readStrippedInput(subpackage: String = "", name: String = "input") =
    File("src/main/kotlin/org/gristle/adventOfCode/$subpackage", "$name.txt")
        .readText()
        .replace("\r", "")

fun String.stripCarriageReturns() = replace("\r", "")

fun String.writeToFile(path: String) {
    File(path)
        .apply {
            parentFile.also {
                if (it != null && !it.exists() && !it.mkdirs()) {
                    throw IllegalStateException("Couldn't create dir: $it")
                }
            }
        }.writeText(this)
}

fun getInput(day: Int, year: Int = LocalDate.now().year, alternate: String = ""): String {
    val inputFile = File("src/main/kotlin/org/gristle/adventOfCode/y$year/d$day/input$alternate.txt")
    if (!inputFile.exists()) {
        if (alternate != "") throw IllegalArgumentException("Sample input specified but not provided!")
        inputFile.parentFile.mkdirs()
        val url = URL("https://adventofcode.com/$year/day/$day/input")
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("Cookie", "session=$SESSION")
        connection.setRequestProperty(
            "User-Agent",
            "github.com/nbanman/Play2022/blob/master/src/main/kotlin/org/gristle/adventOfCode/generate/Generate.kt by neil.banman@gmail.com"
        )
        try {
            connection.connect()
            check(connection.responseCode == 200) { "${connection.responseCode} ${connection.responseMessage}" }
            connection.inputStream.use { input ->
                inputFile.outputStream().use { output ->
                    input.copyTo(output)
                }
                inputFile.setReadOnly()
            }
        } finally {
            connection.disconnect()
        }
    }
    return inputFile.readText().stripCarriageReturns().trimEnd { it == '\n' }
}

fun printColored(text: String, fg: AnsiColor? = null, bg: AnsiColor? = null) {
    val fgCode = fg?.let { "${it.fg}" } ?: ""
    val bgCode = bg?.let { "${it.bg}" } ?: ""
    val sep = if (fgCode.isNotBlank() && bgCode.isNotBlank()) ";" else ""
    print("\u001b[${fgCode}${sep}${bgCode}m$text\u001B[0m")
}

@Suppress("unused")
enum class AnsiColor(val fg: Int, val bg: Int) {
    BLACK(30, 40),
    RED(31, 41),
    GREEN(32, 42),
    YELLOW(33, 43),
    BLUE(34, 44),
    MAGENTA(35, 45),
    CYAN(36, 46),
    WHITE(37, 47),

    BRIGHT_BLACK(90, 100),
    BRIGHT_RED(91, 101),
    BRIGHT_GREEN(92, 102),
    BRIGHT_YELLOW(93, 103),
    BRIGHT_BLUE(94, 104),
    BRIGHT_MAGENTA(95, 105),
    BRIGHT_CYAN(96, 106),
    BRIGHT_WHITE(97, 107),
}