package org.gristle.adventOfCode.y2022.d18

import org.gristle.adventOfCode.utilities.*

class Y2022D18(input: String) {

    // bounds are IntRanges, one for each of three dimensions, giving the boundaries of the droplet with
    // a 1-space buffer on each side
    private val xBound: IntRange
    private val yBound: IntRange
    private val zBound: IntRange

    // set of all cubes in the droplet
    private val cubes: Set<Xyz>

    init {
        // used to calculate the bounds
        var minX = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var minY = Int.MAX_VALUE
        var maxY = Int.MIN_VALUE
        var minZ = Int.MAX_VALUE
        var maxZ = Int.MIN_VALUE

        // parsing bounds and cubes
        cubes = input.getInts().chunked(3).map { (x, y, z) ->
            if (x < minX) minX = x
            if (x > maxX) maxX = x
            if (y < minY) minY = y
            if (y > maxY) maxY = y
            if (z < minZ) minZ = z
            if (z > maxZ) maxZ = z
            Xyz(x, y, z)
        }.toSet()

        xBound = minX - 1..maxX + 1
        yBound = minY - 1..maxY + 1
        zBound = minZ - 1..maxZ + 1
    }

    // utility function that returns the 6 spaces adjacent to a given cube
    private fun Xyz.adjacent() = Xyz.CROSS.map { it + this }

    // for every cube in the droplet, count the number of adjacent spaces that match a given predicate, then
    // sum that count. 
    private inline fun surfaceArea(predicate: (Xyz) -> Boolean): Int = cubes.sumOf { cube ->
        cube.adjacent().count { predicate(it) }
    }

    // Predicate returns true when the space isn't occupied by a cube in the droplet. This fulfils the surface area 
    // rules of Part 1.
    fun part1() = surfaceArea { it !in cubes }

    // Starting from outside the cube, find all the spaces within the bounds that do not contain a droplet cube.
    // The predicate returns true when the space is one of those exterior spaces. This fulfils the surface area rules
    // of Part 2.
    fun part2(): Int {
        // Use a BFS flood fill starting from outside the droplet. Since the bounds ranges allow a space of at least
        // one in every dimension, the BFS will go around the entire droplet and try to penetrate it.
        // Returns a set of points in and around the droplet that are part of the exterior.
        val exterior: Set<Xyz> = Graph
            .bfsSequence(
                startId = Xyz(xBound.first, yBound.first, zBound.first),
                defaultEdges = { pos ->
                    pos.adjacent().filter {
                        !cubes.contains(it) // the space is not a cube
                                && it.x in xBound // is in-bounds on x-axis
                                && it.y in yBound // y-axis
                                && it.z in zBound // z-axis
                    }
                }
            ).map { it.id }
            .toSet()

        return surfaceArea { it in exterior }
    }
}

fun main() {
    val input = getInput(18, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D18(input)
    println("Class creation: ${timer.lap()}ms") // 58ms
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 4332 (40ms)
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 2524 (80ms) (1286ms original)
    println("Total time: ${timer.elapsed()}ms") // 179ms
}