package org.gristle.adventOfCode.utilities

import java.io.File

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
