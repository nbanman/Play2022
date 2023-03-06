package org.gristle.adventOfCode.y2018.d9

import org.gristle.adventOfCode.Day

class Y2018D9(private val input: String) : Day {

    private val pattern = """(\d+) players; last marble is worth (\d+) points""".toRegex()

    class Dll<E>(val value: E, leftNode: Dll<E>? = null, rightNode: Dll<E>? = null) {
        var left = leftNode ?: this
        var right = rightNode ?: this
        fun addRight(other: Dll<E>): Dll<E> {
            other.left = this
            other.right = right
            right.left = other
            right = other
            return other
        }

        tailrec fun goLeft(n: Int, dll: Dll<E> = this): Dll<E> {
            if (n == 0) return dll 
            return goLeft(n - 1, dll.left)
        }

        fun remove(): Dll<E> {
            left.right = right
            right.left = left
            return right
        }

        override fun toString() = "$value"
    }

    data class Score(var amt: Long)

    fun solve(multiplier: Int = 1): Long {
        val gv = pattern.find(input)?.groupValues ?: throw IllegalArgumentException()
        val players = gv[1].toInt()
        val lastMarble = gv[2].toLong() * multiplier
        val scores = List(players) { Score(0) }
        var currentMarble = Dll(0)
        for (x in 1..lastMarble) {
            if (x % 23 == 0L) {
                currentMarble = currentMarble.goLeft(7)
                scores[((x - 1) % players).toInt()].amt += x + currentMarble.value
                currentMarble = currentMarble.remove()
            } else {
                currentMarble = currentMarble.right.addRight(Dll(x.toInt()))
            }
        }
        return scores.maxOf { it.amt }
    }

    override fun part1() = solve()

    override fun part2() = solve(100)
}

fun main() = Day.runDay(Y2018D9::class)

//    Class creation: 17ms
//    Part 1: 422980 (11ms)
//    Part 2: 3552041936 (505ms)
//    Total time: 533ms