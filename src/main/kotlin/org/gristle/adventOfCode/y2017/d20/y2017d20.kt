package org.gristle.adventOfCode.y2017.d20

import org.gristle.adventOfCode.utilities.*
import kotlin.math.abs

// not refactored
object Y2017D20 {
    private val input = readRawInput("y2017/d20")

    data class Vector3D(val x: Int, val y: Int, val z: Int) {
        fun sum() = abs(x) + abs(y) + abs(z)
        operator fun plus(other: Vector3D) = Vector3D(x + other.x, y + other.y, z + other.z)
    }

    data class Particle(val number: Int, val p: Vector3D, val v: Vector3D, val a: Vector3D) {
        fun manhattanDistance() = p.sum()
        fun stableTime(): Int {
            return listOf(
                stableAxis(v.x, a.x),
                stableAxis(v.y, a.y),
                stableAxis(v.z, a.z),
            ).maxOf { it }
        }
        fun particleAt(time: Int): Particle {
            if (time == 0) return this
            val newV = v + a
            val newP = p + newV
            val newParticle = Particle(number, newP, newV, a)
            return newParticle.particleAt(time - 1)
        }

        private fun stableAxis(v: Int, a: Int): Int {
            return if (v * a >= 0) {
                0
            } else {
                abs(v / a) + 1
            }
        }
    }

    private val pattern = """p=<(-?\d+),(-?\d+),(-?\d+)>, v=<(-?\d+),(-?\d+),(-?\d+)>, a=<(-?\d+),(-?\d+),(-?\d+)>"""

    val particles = input
        .groupValues(pattern)
        .mapIndexed { index, gv ->
            Particle(
                index,
                Vector3D(gv[0].toInt(), gv[1].toInt(), gv[2].toInt()),
                Vector3D(gv[3].toInt(), gv[4].toInt(), gv[5].toInt()),
                Vector3D(gv[6].toInt(), gv[7].toInt(), gv[8].toInt())
            )
        }.sortedBy { it.a.sum() }

    fun part1(): Int {
        val selectParticles = particles.filter {
            it.a.sum() == particles.first().a.sum()
        }
        //    selectParticles.forEach { println("time: ${it.stableTime()}, $it") }
        val offset = selectParticles.maxOf { it.stableTime() }
        return selectParticles.maxByOrNull { it.particleAt(offset).manhattanDistance() }!!.number
    }

    fun part2(): Int {
        return (0..1000).fold(particles) { acc, _ ->
            val collisionsRemoved = acc
                .groupBy { it.p }
                .filter { it.value.size == 1 }
                .map { it.value.first() }
            collisionsRemoved.map { it.particleAt(1) }
        }.size
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2017D20.part1()} (${elapsedTime(time)}ms)") // 308
    time = System.nanoTime()
    println("Part 2: ${Y2017D20.part2()} (${elapsedTime(time)}ms)") // 504
}