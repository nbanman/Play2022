package org.gristle.adventOfCode.utilities

import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate

const val SESSION =
    "53616c7465645f5fb11066d92ddc288fc43ef3462bc477713612df0d0abce56129c3e9f3ebe89a4c22373556bc48623fea51bc240fd55e24089442677e7e8cd0"

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
            }
        } finally {
            connection.disconnect()
        }
    }
    return inputFile.readText().stripCarriageReturns().trimEnd()
}