package org.gristle.adventOfCode.generate

import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.writeToFile
import java.time.LocalDate

fun makeDay(day: Int, year: Int = LocalDate.now().year) {
    readRawInput("generate", "template")
        .replace("[[YEAR]]", year.toString())
        .replace("[[DAY]]", day.toString())
        .writeToFile("src/main/kotlin/org/gristle/adventOfCode/y${year}/d$day/Y${year}D$day.kt")
}

fun main(args: Array<String>) {
    for (day in 3..25) {
        makeDay(day)
    }
    return
    val day: Int
    val year: Int
    val thisYear = LocalDate.now().year

    when (args.size) {
        0 -> {
            println("Enter Day:")
            day = readln().toInt()
            println("Enter Year, or Enter for [$thisYear]")
            year = readln().let { if (it == "") thisYear else it.toInt() }
        }

        1 -> {
            day = args.first().toIntOrNull() ?: -1
            if (day !in 1..31) {
                println("The day must be an integer between 1 and 31.")
                return
            }
            year = LocalDate.now().year
        }

        else -> {
            day = args.first().toIntOrNull() ?: -1
            if (day !in 1..31) {
                println("The day must be an integer between 1 and 31.")
                return
            }

            year = (args[1].toIntOrNull() ?: -1).let {
                if (it == -1) {
                    println("The year must be an integer.")
                    return
                }
                if (it < 100) it + 2000 else it
            }
        }
    }
    makeDay(day, year)
}



