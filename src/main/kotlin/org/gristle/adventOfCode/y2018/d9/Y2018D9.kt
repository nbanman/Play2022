package org.gristle.adventOfCode.y2018.d9

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList

class Y2018D9(input: String) : Day {

    private val players: Int
    private val highestValue: Int

    init {
        input.getIntList().let { (a, b) ->
            players = a
            highestValue = b
        }
    }

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
        val lastMarble = highestValue * multiplier
        val scores = List(players) { Score(0) }
        var currentMarble = Dll(0)
        for (x in 1..lastMarble) {
            if (x % 23 == 0) {
                currentMarble = currentMarble.goLeft(7)
                scores[((x - 1) % players)].amt += x + currentMarble.value
                currentMarble = currentMarble.remove()
            } else {
                currentMarble = currentMarble.right.addRight(Dll(x))
            }
        }
        return scores.maxOf(Score::amt)
    }

    override fun part1() = solve()

    override fun part2() = solve(100)
}

fun main() = Day.runDay(Y2018D9::class)

//    Class creation: 17ms
//    Part 1: 422980 (11ms)
//    Part 2: 3552041936 (505ms)
//    Total time: 533ms