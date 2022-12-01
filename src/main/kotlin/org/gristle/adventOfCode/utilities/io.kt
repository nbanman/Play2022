package org.gristle.adventOfCode.utilities

import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate

const val SESSION =
    "53616c7465645f5fbbc88264793b035d3d6856e76640af009bb1141bb9b34c90fa961d9e5f26cc573eb7a34cf3bf350178eb8803d69c6805032395b133b4be58"

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

fun getInput(day: Int, year: Int = LocalDate.now().year) {
    val inputFile = File("src/main/kotlin/org/gristle/adventOfCode/y$year/d$day/input.txt")
    if (!inputFile.exists()) {
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
            }
        } finally {
            connection.disconnect()
        }
    }
}