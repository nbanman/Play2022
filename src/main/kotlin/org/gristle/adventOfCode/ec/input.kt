package org.gristle.adventOfCode.ec

import java.io.File

fun ecInputs(year: Int, day: Int) = (1..3).map { part -> ecPart(year, day, part) }

fun ecPart(year: Int, day: Int, part: Int): String {
    val day = if (day < 10) "0$day" else day.toString()
    val fileName = "src/main/kotlin/org/gristle/adventOfCode/ec/inputs/everybody_codes_e20${year}_q${day}_p${part}.txt"
    return File(fileName)
        .readText()
        .replace("\r", "")
        .trimEnd()
}
