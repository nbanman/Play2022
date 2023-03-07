package org.gristle.adventOfCode.y2019.d6

import org.gristle.adventOfCode.Day

class Y2019D6(input: String) : Day {

    data class CelestialBody(val name: String, private val parentName: String) {
        companion object {
            // register is a mutable hashmap shared by all instances used to get a parent from the parent's name
            private val register = mutableMapOf<String, CelestialBody>()

            // public getter of register
            operator fun get(name: String): CelestialBody = register[name]
                ?: throw IllegalArgumentException("No CelestialBody in register with name '$name.'")
        }

        // init registers the instance to the shared register.
        init { register[name] = this }
        private val parent: CelestialBody? get() = register[parentName]
        // orbits and path are recursive, adding information provided by their parent until the parent is null.
        // But rather than functions, they are lazy variables, so each instance only calculates once, and their
        // calculation is deferred until all the instances are created.
        val orbits: Int by lazy { parent?.orbits?.plus(1) ?: 0 }
        val path: List<CelestialBody> by lazy { (parent?.path ?: emptyList()) + this }
    }

    // Create objects from input
    private val celestialBodies = input
        .lines()
        .map { line -> line
            .split(')')
            .let { CelestialBody(it.last(), it.first()) }
        } + CelestialBody("COM", "")

    override fun part1() = celestialBodies.sumOf(CelestialBody::orbits)

    override fun part2(): Int {
        // Part 2. Get paths from the two objects, find how much of a path they share, sum the lengths
        // of the two paths, subtract the shared lengths from the sum.
        val me = CelestialBody["YOU"].path.dropLast(1)
        val santa = CelestialBody["SAN"].path.dropLast(1)
        val sharedSize = me.indices.first { i -> me[i] != santa[i] }
        return me.size + santa.size - sharedSize * 2
    }
}


fun main() = Day.runDay(Y2019D6::class)

//    Class creation: 35ms
//    Part 1: 315757 (1ms)
//    Part 2: 481 (0ms)
//    Total time: 37ms