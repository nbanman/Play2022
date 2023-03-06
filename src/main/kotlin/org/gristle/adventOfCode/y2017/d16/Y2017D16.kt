package org.gristle.adventOfCode.y2017.d16

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues

// not refactored!
class Y2017D16(input: String) : Day {

    private data class Command(val name: String, val arg1: String, val arg2: String) {
        fun execute(line: String): String {
            return when (name) {
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

                else -> throw IllegalArgumentException("Bad input: $name")
            }
        }

        private fun exchange(line: String, indexA: Int, indexB: Int): String {
            return line
                .replace(line[indexA], 'z')
                .replace(line[indexB], line[indexA])
                .replace('z', line[indexB])
        }
    }

    private fun danceParty(start: String) =
        commands.fold(start) { acc, command -> command.execute(acc) }

    private val pattern = """([psx])([a-p\d]+)(?:/([a-p\d]+))?""".toRegex()
    private val commands = input
        .groupValues(pattern)
        .map { Command(it[0], it[1], it[2]) }

    private val start = "abcdefghijklmnop"
    private val part1 = danceParty(start)

    override fun part1() = part1

    override fun part2(): String {
        val register = mutableSetOf(start, part1)
        while (true) {
            val new = danceParty(register.last())
            if (new in register) break else register.add(new)
        }
        return register.elementAt(1_000_000_000 % register.size)
    }
}

fun main() = Day.runDay(16, 2017, Y2017D16::class)

//    Class creation: 57ms
//    Part 1: hmefajngplkidocb (0ms)
//    Part 2: fbidepghmjklcnoa (146ms)
//    Total time: 204ms