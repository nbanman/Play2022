package org.gristle.adventOfCode.test

import org.gristle.adventOfCode.utilities.groupValues
import java.io.File

private fun String.toAnswer() = when {
    toIntOrNull() != null -> this
    toLongOrNull() != null -> this
    this == "true" -> this
    this == "false" -> this
    else -> """"$this""""
}

fun main() {
    val report = File("src/main/kotlin/org/gristle/adventOfCode/test", "Test Results - Test2022.xml").readText()
    val pattern = Regex(
        """<test duration=\"\d+\" locationUrl=\"java:test://org\.gristle\.adventOfCode\.utilities\.Test\d+\.y(\d+)d(\d+)\${'$'}Play2022\" name=\"[^>]+>\r\n +<diff actual=\"\(([^,]+), ([^)]+)"""
    )
    val tests = report
        .groupValues(pattern)
        .map { (year, day, pt1, pt2) ->
            val pt1Answer = pt1.toAnswer()
            val pt2Answer = pt2.toAnswer()
            day.toInt() to "@Test\n" +
                    "    internal fun y${year}d${day}() {\n" +
                    "        assertEquals(${pt1Answer} to ${pt2Answer}, Day.testDay(Y${year}D${day}::class))\n" +
                    "    }\n" +
                    "\n"
        }.sortedBy { it.first }
        .joinToString("") { it.second }

    println(tests)
}