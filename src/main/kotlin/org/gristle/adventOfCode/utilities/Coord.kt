package org.gristle.adventOfCode.utilities

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Coord(val x: Int, val y: Int) : Comparable<Coord> {
    companion object {
        val ORIGIN = Coord(0, 0)
        fun fromIndex(n: Int, width: Int) = Coord(n % width, n / width)

        fun forRectangle(tl: Coord, br: Coord, action: (x: Int, y: Int) -> Unit) {
            for (y in tl.y..br.y) for (x in tl.x..br.x) action(x, y)
        }

        fun forRectangle(xRange: IntRange, yRange: IntRange, action: (x: Int, y: Int) -> Unit) {
            for (y in yRange) for (x in xRange) action(x, y)
        }

        fun forRectangle(minMaxRange: Pair<IntRange, IntRange>, action: (x: Int, y: Int) -> Unit) =
            forRectangle(minMaxRange.first, minMaxRange.second, action)
    }

    fun asIndex(width: Int) = y * width + x
    operator fun plus(coord: Coord) = Coord(x + coord.x, y + coord.y)

    operator fun plus(n: Int) = Coord(x + n, y + n)

    operator fun minus(coord: Coord) = Coord(x - coord.x, y - coord.y)

    operator fun times(other: Coord) = Coord(x * other.x, y * other.y)

    operator fun rem(other: Coord) = Coord(x % other.x, y % other.y)

    infix fun fmod(other: Coord) = Coord(x fmod other.x, y fmod other.y)

    fun max(other: Coord) = Coord(max(x, other.x), max(y, other.y))

    fun min(other: Coord) = Coord(min(x, other.x), min(y, other.y))

    fun area() = x * y

    override fun toString() = "($x, $y)"

    fun move(distance: Int, size: Coord, wrapAround: Boolean, operation: () -> Coord): Coord {
        return (this + (Coord(distance, distance) * operation()))
            .let {
                if (size == Coord(0,0)) it else {
                    if (wrapAround) it fmod size else Coord(
                        it.x.coerceIn(0 until size.x),
                        it.y.coerceIn(0 until size.y)
                    )
                }
            }
    }
    fun north(distance: Int = 1, size: Coord = Coord(0, 0), wrapAround: Boolean = false) =
        move(distance, size, wrapAround) { Coord(0, -1) }
    fun northwest(distance: Int = 1, size: Coord = Coord(0, 0), wrapAround: Boolean = false) =
        move(distance, size, wrapAround) { Coord(-1, -1) }
    fun northeast(distance: Int = 1, size: Coord = Coord(0, 0), wrapAround: Boolean = false) =
        move(distance, size, wrapAround) { Coord(1, -1) }
    fun south(distance: Int = 1, size: Coord = Coord(0, 0), wrapAround: Boolean = false) =
        move(distance, size, wrapAround) { Coord(0, 1) }
    fun southwest(distance: Int = 1, size: Coord = Coord(0, 0), wrapAround: Boolean = false) =
        move(distance, size, wrapAround) { Coord(-1, 1) }
    fun southeast(distance: Int = 1, size: Coord = Coord(0, 0), wrapAround: Boolean = false) =
        move(distance, size, wrapAround) { Coord(1, 1) }
    fun west(distance: Int = 1, size: Coord = Coord(0, 0), wrapAround: Boolean = false) =
        move(distance, size, wrapAround) { Coord(-1, 0) }
    fun east(distance: Int = 1, size: Coord = Coord(0, 0), wrapAround: Boolean = false) =
        move(distance, size, wrapAround) { Coord(1, 0) }

    fun getNeighbors(includeDiagonals: Boolean = false): List<Coord> {
        return if (includeDiagonals) {
            listOf(northwest(), north(), northeast(), east(), southeast(), south(), southwest(), west())
        } else {
            listOf(north(), east(), south(), west())
        }
    }

    fun getNeighbors(distance: Int, includeSelf: Boolean = true): List<Coord> {
        val tl = Coord(x - distance, y - distance)
        val br = Coord(x + distance, y + distance)
        val neighbors = mutableListOf<Coord>()
        forRectangle(tl, br) { x, y -> neighbors.add(Coord(x, y)) }
        if (!includeSelf) neighbors.remove(this)
        return neighbors
    }

    fun manhattanDistance(coord: Coord = Coord.ORIGIN): Int = abs(x - coord.x) + abs(y - coord.y)

    fun move(dir: Nsew, distance: Int = 1) = when (dir) {
        Nsew.NORTH -> north(distance)
        Nsew.SOUTH -> south(distance)
        Nsew.EAST -> east(distance)
        Nsew.WEST -> west(distance)
    }

    override fun compareTo(other: Coord) = manhattanDistance() - other.manhattanDistance()
}

fun Int.asCoord(width: Int) = Coord(this % width, this / width)

fun Iterable<Coord>.minMaxRanges(): Pair<IntRange, IntRange> {
    var minX = Integer.MAX_VALUE
    var minY = Integer.MAX_VALUE
    var maxX = Int.MIN_VALUE
    var maxY = Int.MIN_VALUE
    forEach { coord ->
        if (coord.x < minX) minX = coord.x
        if (coord.x > maxX) maxX = coord.x
        if (coord.y < minY) minY = coord.y
        if (coord.y > maxY) maxY = coord.y
    }
    return minX..maxX to minY..maxY
}

fun Iterable<Coord>.printToConsole(blankSpace: Char = '.') {
    val (xRange, yRange) = minMaxRanges()
    Coord.forRectangle(xRange, yRange) { x, y ->
        if (x == xRange.first && y != yRange.first) print('\n')
        print(if (Coord(x, y) in this) '#' else blankSpace)
    }
    println("\n")
}

