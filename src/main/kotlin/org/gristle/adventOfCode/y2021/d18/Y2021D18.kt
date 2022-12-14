package org.gristle.adventOfCode.y2021.d18

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import java.util.*
import kotlin.math.ceil

class Y2021D18(input: String) {

    data class Snailfish constructor(val expression: String) {

        companion object {
            fun fromString(expression: String): Snailfish {

                fun String.validated(): String {

                    val bracketPattern = Regex("""^\[(\d{1,2}),(\d{1,2})]""")
                    val lastNumberPattern = Regex("""(\d+)\D*${'$'}""")
                    val nextNumberPattern = Regex("""^\D*(\d+)""")

                    // look for explodes
                    val bracketStack = Stack<Int>()
                    for (i in indices) {
                        when (this[i]) {
                            '[' -> bracketStack.push(i)
                            ']' -> bracketStack.pop()
                        }
                        if (bracketStack.size == 5) {
                            val bracketMatch = bracketPattern.find(substring(i))!!
                            val lastNumberMatch = lastNumberPattern
                                .find(substring(0, bracketMatch.range.first + i))
                            val nextNumberMatch = nextNumberPattern
                                .find(substring(bracketMatch.range.last + 1 + i))
                            val replacement = buildString {
                                if (lastNumberMatch != null) {
                                    val newNumber = lastNumberMatch.groupValues[1].toInt() +
                                            bracketMatch.groupValues[1].toInt()
                                    append(newNumber)
                                    append(lastNumberMatch.value.dropWhile { it.isDigit() })
                                }
                                append('0')
                                if (nextNumberMatch != null) {
                                    val newNumber = nextNumberMatch.groupValues[1].toInt() +
                                            bracketMatch.groupValues[2].toInt()
                                    append(nextNumberMatch.value.dropLastWhile { it.isDigit() })
                                    append(newNumber)
                                }
                            }
                            val left = lastNumberMatch?.range?.first ?: (bracketMatch.range.first + i)
                            val right = 1 + i + bracketMatch.range.last + (nextNumberMatch?.value?.length ?: 0)
                            val newExpression = substring(0, left) + replacement + substring(right)
                            return newExpression.validated()
                        }
                    }
                    // look for splits
                    val splitPattern = Regex("""\d{2,}""")
                    splitPattern.find(this)?.let { split ->
                        val splitInt = split.value.toInt()
                        val replacement = "[${ splitInt / 2 },${ ceil(splitInt / 2.0).toInt() }]"
                        val newExpression = substring(0, split.range.first) +
                                replacement +
                                substring(split.range.last + 1)
                        return newExpression.validated()
                    }
                    return this
                }

                return Snailfish(expression.validated())
            }
        }

        operator fun plus(other: Snailfish): Snailfish {
            val addedExpression = buildString {
                append('[')
                append(expression)
                append(',')
                append(other.expression)
                append(']')
            }
            return fromString(addedExpression)
        }

        fun magnitude(): Long {
            // [[1,2],[[3,4],5]]
            // [1,2] --- [[3,4],5]
            //   6   --- [3,4],5
            //             17
            fun split(expression: String): Long {
                expression.toLongOrNull()?.let { return it }
                var bracket = 0
                val left = expression.drop(1).takeWhile {
                    when (it) {
                        '[' -> bracket++
                        ']' -> bracket--
                    }
                    !(bracket == 0 && it == ',')
                }
                val right = expression.dropLast(1).substring(left.length + 2)
                return split(left) * 3 + split(right) * 2
            }
            return split(expression)
        }

        override fun toString(): String {
            return expression
        }
    }

    private val snailfish = input.lines().map(Snailfish::fromString)

    fun part1(): Long {
        val sum = snailfish.reduce(Snailfish::plus)
        return sum.magnitude()
    }

    fun part2() = snailfish
        .flatMapIndexed { index: Int, sf: Snailfish ->
            ((index + 1)..snailfish.lastIndex).map { sf2Index -> (sf + snailfish[sf2Index]).magnitude() }
        }.max()
}

fun main() {
    var time = System.nanoTime()
    val c = Y2021D18(readRawInput("y2021/d18"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 3806
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 4727
}