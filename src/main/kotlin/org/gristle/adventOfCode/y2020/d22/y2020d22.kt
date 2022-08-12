package org.gristle.adventOfCode.y2020.d22

import org.gristle.adventOfCode.utilities.*
import kotlin.math.min

object Y2020D22 {
    private val input = readStrippedInput("y2020/d22")

    val cardNos = input
        .split("\n\n")
        .map { half -> half
            .split('\n')
            .mapNotNull { it.toIntOrNull() }
        }

    val p1 = cardNos.first()
    val p2 = cardNos.last()

    fun List<Int>.score() = foldIndexed(0) { index, acc, i -> acc + (size - index) * i }

    fun part1(): Int {
        tailrec fun play(p1: List<Int>, p2: List<Int>): List<Int> {
            return when {
                p1.isEmpty() -> p2
                p2.isEmpty() -> p1
                else -> {
                    val short = min(p1.size, p2.size)
                    val (p1Wins, p2Wins) = p1
                        .zip(p2)
                        .partition { ab -> ab.first > ab.second }
                    play(
                        p1.drop(short) + p1Wins.flatMap { it.toList().sortedDescending() },
                        p2.drop(short) + p2Wins.flatMap { it.toList().sortedDescending() }
                    )
                }
            }
        }

        return play(p1, p2).score()
    }

    fun part2(): Int {

        fun play(p1: List<Int>, p2: List<Int>): Pair<String, List<Int>> {
            val cache = mutableSetOf<Pair<List<Int>, List<Int>>>()
            tailrec fun play2(p1: List<Int>, p2: List<Int>): Pair<String, List<Int>> {

                if (cache.contains(p1 to p2)) {
                    return "p1" to p1
                } else {
                    cache.add(p1 to p2)
                }
                val p1New: List<Int>
                val p2New: List<Int>
                when {
                    p1.isEmpty() -> return "p2" to p2
                    p2.isEmpty() -> return "p1" to p1
                    else -> {
                        val p1Poll = p1.first()
                        val p2Poll = p2.first()
                        if (p1.size - 1 < p1Poll || p2.size - 1 < p2Poll) {
                            if (p1Poll > p2Poll) {
                                p1New = p1.drop(1) + listOf(p1Poll, p2Poll)
                                p2New = p2.drop(1)
                            } else {
                                p1New = p1.drop(1)
                                p2New = p2.drop(1) + listOf(p2Poll, p1Poll)
                            }
                        } else {
                            val p1Mini = p1.drop(1).take(p1Poll)
                            val p2Mini = p2.drop(1).take(p2Poll)
                            val subGame = play(p1Mini, p2Mini)
                            if (subGame.first == "p1") {
                                p1New = p1.drop(1) + listOf(p1Poll, p2Poll)
                                p2New = p2.drop(1)
                            } else {
                                p1New = p1.drop(1)
                                p2New = p2.drop(1) + listOf(p2Poll, p1Poll)
                            }
                        }
                    }
                }
                return play2(p1New, p2New)
            }
            return play2(p1, p2)
        }
        return play(p1, p2).second.score()
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D22.part1()} (${elapsedTime(time)}ms)") // 32824
    time = System.nanoTime()
    println("Part 2: ${Y2020D22.part2()} (${elapsedTime(time)}ms)") // 36515
}