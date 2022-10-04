package org.gristle.adventOfCode.y2016.d21

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

// Not refactored, but not horrible. Maybe I'm getting better!
class Y2016D21(input: String) {
    data class Command(val name: String, val pos1: String, val pos2: String) {
        private fun String.rotateLeft(n: Int) = rotate(n)

        private fun String.rotateRight(n: Int) = rotate(0 - n)

        private fun String.rotate(n: Int): String {
            val p = kotlin.math.abs(n % length)
            return if (n >= 0) {
                drop(p) + take(p)
            } else {
                takeLast(p) + dropLast(p)
            }
        }

        fun execute(s: String, reversed: Boolean = false): String {
            val rString = when (name) {
                "swap position" -> {
                    val p1 = pos1.toInt()
                    val p2 = pos2.toInt()
                    val answer = s
                        .replace(s[p1], '1')
                        .replace(s[p2], s[p1])
                        .replace('1', s[p2])
                    answer
                }
                "swap letter" -> {
                    val answer = s
                        .replace(pos1, "1")
                        .replace(pos2, pos1)
                        .replace("1", pos2)
                    answer
                }
                "reverse positions" -> {
                    val range = pos1.toInt()..pos2.toInt()
                    val sub = s.slice(range).reversed()
                    val answer = s.substring(0, range.first) + sub + s.substring(range.last + 1)
                    answer
                }
                "rotate left" -> {
                    val p1 = pos1.toInt()
                    if (reversed) {
                        s.rotateRight(p1)
                    } else {
                        s.rotateLeft(p1)
                    }
                }

                "rotate right" -> {
                    val p1 = pos1.toInt()
                    if (reversed) {
                        s.rotateLeft(p1)
                    } else {
                        s.rotateRight(p1)
                    }
                }

                "rotate based" -> {
                    val index = s.indexOf(pos1)
                    val rotations = if (reversed) {
                        (index / 2 + (if (index % 2 == 1 || index == 0) 1 else 5))
                    } else {
                        -(index + 1 + (if (index >= 4) 1 else 0))
                    }
                    s.rotate(rotations)
                }

                "move position" -> {
                    val (p1, p2) = if(reversed) pos2.toInt() to pos1.toInt() else pos1.toInt() to pos2.toInt()
                    val answer = s
                        .replace(s[p1].toString(), "")
                        .let { it.substring(0, p2) + s[p1] + it.substring(p2) }
                    answer
                }

                else -> "Invalid"
            }
            return rString
        }
    }

    private val passcode = "abcdefgh"
    private val passcode2 = "fbgdceah"

    private val pattern = """(\w+ \w+) (?:on position of letter )?(\w+)(?:.* (\w+))?""".toRegex()

    val commands = input
        .groupValues(pattern)
        .map { Command(it[0], it[1], it[2]) }

    fun part1() = commands.fold(passcode) { acc, command ->
        command.execute(acc)
    }

    fun part2() = commands.reversed().fold(passcode2) { acc, command ->
        command.execute(acc, reversed = true)
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D21(readRawInput("y2016/d21"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // bfheacgd
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // gcehdbfa
}