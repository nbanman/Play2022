package org.gristle.adventOfCode.generate

import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.writeToFile
import java.net.URL
import java.time.LocalDate

fun main(args: Array<String>) {
    val day: String
    val year: String

    when (args.size) {
        0 -> {
            println("Please provide a day and optionally a year, separated by spaces.")
            return
        }

        1 -> {
            val dayInt = args.first().toIntOrNull() ?: -1
            if (dayInt !in 1..31) {
                println("The day must be an integer between 1 and 31.")
                return
            } else {
                day = dayInt.toString()
            }
            year = LocalDate.now().year.toString()
        }

        else -> {
            val dayInt = args.first().toIntOrNull() ?: -1
            if (dayInt !in 1..31) {
                println("The day must be an integer between 1 and 31.")
                return
            } else {
                day = dayInt.toString()
            }
            val yearInt = (args[1].toIntOrNull() ?: -1).let {
                if (it == -1) {
                    println("The year must be an integer.")
                    return
                }
                if (it < 100) it + 2000 else it
            }
            year = yearInt.toString()
        }
    }
    // Make kt file
    readRawInput("generate", "template")
        .replace("[[YEAR]]", year)
        .replace("[[DAY]]", day)
        .writeToFile("src/main/kotlin/org/gristle/adventOfCode/y${year}/d$day/Y${year}D$day.kt")

    // Make input.txt file

    // First get from web
    val url = URL("https://adventofcode.com/2022/day/1/input")
    val con = url.openConnection()
    con.setRequestProperty("User-Agent")


}

