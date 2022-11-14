package org.gristle.adventOfCode.y2022

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readStrippedInput

class Template(input: String) {


    fun part1() = "To be implemented"

    fun part2() = "To be implemented"
}

fun main() {
    var time = System.nanoTime()
    val c = Template(readStrippedInput("y2022/d"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 
}