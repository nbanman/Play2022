package org.gristle.adventOfCode.utilities

import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate

const val SESSION =
    "53616c7465645f5fdf37ca6e2c5944bcfbb96ff7f2c8c3f0e9519bb07aeb30eb920d6dfb3e9e65131a7d078b3d4a249e587e347008e521f2cf78501980297bc5"

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
    return inputFile.readText().stripCarriageReturns().trimEnd()
}