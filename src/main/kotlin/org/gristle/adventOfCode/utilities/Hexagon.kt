package org.gristle.adventOfCode.utilities

import kotlin.math.abs

data class Hexagon(val q: Int = 0, val r: Int = 0): Comparable<Hexagon> {
    companion object {
        val origin = Hexagon(0, 0)
    }

    val s = -q - r

    fun hexAt(step: String): Hexagon {
        return when (step) {
            "n" -> Hexagon(q, r - 1)
            "s" -> Hexagon(q, r + 1)
            "nw" -> Hexagon(q - 1, r)
            "ne" -> Hexagon(q + 1, r - 1)
            "sw" -> Hexagon(q - 1, r + 1)
            "se" -> Hexagon(q + 1, r)
            else -> Hexagon(q, r)
        }
    }

    override fun toString() = "Hex(q=$q, r=$r, s=$s)"

    private fun cubeSubtract(h: Hexagon): Hexagon {
        return Hexagon(q - h.q, r - h.r)
    }

    fun distance(h: Hexagon = origin): Int {
        val vec = cubeSubtract(h)
        return maxOf(abs(vec.q), abs(vec.r), abs(vec.s))
    }

    fun neighbors() = listOf(
        hexAt("nw"),
        hexAt("n"),
        hexAt("ne"),
        hexAt("se"),
        hexAt("s"),
        hexAt("sw")
    )

    override fun compareTo(other: Hexagon): Int {
        return distance() - other.distance()
    }
}