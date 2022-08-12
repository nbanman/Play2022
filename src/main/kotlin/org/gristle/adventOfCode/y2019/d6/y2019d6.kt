package org.gristle.adventOfCode.y2019.d6

import org.gristle.adventOfCode.utilities.*

object Y2019D6 {
    private val input = readInput("y2019/d6")

    data class CelestialBody(val name: String, private val parentName: String) {
        companion object {
            // register is a mutable hashmap shared by all instances used to get a parent from the parent's name
            private val register = mutableMapOf<String, CelestialBody>()
            // public getter of register
            operator fun get(name: String): CelestialBody? = register[name]
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
        .map { line -> line
            .split(')')
            .let { CelestialBody(it.last(), it.first()) }
        } + CelestialBody("COM", "")

    fun part1() = celestialBodies.sumOf { it.orbits }

    fun part2(): Int {
        // Part 2. Get paths from the two objects, find how much of a path they share, sum the lengths
        // of the two paths, subtract the shared lengths from the sum.
        val me = CelestialBody["YOU"]?.path?.dropLast(1) ?: throw IllegalStateException()
        val santa = CelestialBody["SAN"]?.path?.dropLast(1) ?: throw IllegalStateException()
        val sharedSize = me.indices.first { i -> me[i] != santa[i] }
        return me.size + santa.size - sharedSize * 2
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2019D6.part1()} (${elapsedTime(time)}ms)") // 315757
    time = System.nanoTime()
    println("Part 2: ${Y2019D6.part2()} (${elapsedTime(time)}ms)") // 481 
}