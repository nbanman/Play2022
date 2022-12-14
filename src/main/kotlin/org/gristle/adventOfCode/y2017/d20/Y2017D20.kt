package org.gristle.adventOfCode.y2017.d20

import org.gristle.adventOfCode.utilities.Xyz
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput
import kotlin.math.abs

// not refactored
class Y2017D20(input: String) {

    data class Particle(val number: Int, val p: Xyz, val v: Xyz, val a: Xyz) {
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
        .toRegex()

    private val particles = input
        .groupValues(pattern)
        .mapIndexed { index, gv ->
            Particle(
                index,
                Xyz(gv[0].toInt(), gv[1].toInt(), gv[2].toInt()),
                Xyz(gv[3].toInt(), gv[4].toInt(), gv[5].toInt()),
                Xyz(gv[6].toInt(), gv[7].toInt(), gv[8].toInt())
            )
        }.sortedBy { it.a.manhattanDistance() }

    fun part1(): Int {
        val selectParticles = particles.filter {
            it.a.manhattanDistance() == particles.first().a.manhattanDistance()
        }
        val offset = selectParticles.maxOf { it.stableTime() }
        return selectParticles
            .maxByOrNull { it.particleAt(offset).p.manhattanDistance() }
            ?.number
            ?: throw Exception("selectParticles has no elements")
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
    val c = Y2017D20(readRawInput("y2017/d20"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 308
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 504
}