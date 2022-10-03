package org.gristle.adventOfCode.y2017.d16

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

// not refactored!
class Y2017D16(input: String) {

    private data class Com(val name: String, val arg1: String, val arg2: String) {
        fun execute(line: String): String {
            return when(name) {
                "s" -> {
                    val split = line.length - arg1.toInt()
                    line.substring(split) + line.substring(0, split)
                }
                "x" -> {
                    exchange(line, arg1.toInt(), arg2.toInt())
                }
                "p" -> {
                    val indexA = line.indexOf(arg1)
                    val indexB = line.indexOf(arg2)
                    exchange(line, indexA, indexB)
                }
                else -> { line }
            }
        }

        fun exchange(line: String, indexA: Int, indexB: Int): String {
            return line
                .replace(line[indexA], 'z')
                .replace(line[indexB], line[indexA])
                .replace('z', line[indexB])
        }
    }

    private fun danceParty(start: String) =
        commands.fold(start) { acc, command ->
            command.execute(acc)
        }

    private val pattern = """([psx])([a-p\d]+)(?:/([a-p\d]+))?""".toRegex()
    private val commands = input
        .groupValues(pattern)
        .map { Com(it[0], it[1], it[2]) }

    private val start = "abcdefghijklmnop"
    private val p1 = danceParty(start)

    fun part1() = p1

    fun part2(): String {
        val register = mutableListOf(start, p1)
        while (true) {
            val new = danceParty(register.last())
            if (new in register) break else register.add(new)
        }
        return register[1_000_000_000 % register.size]
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2017D16(readRawInput("y2017/d16"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // hmefajngplkidocb
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // fbidepghmjklcnoa
}