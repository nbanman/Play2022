package org.gristle.adventOfCode.y2017.d20

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Xyz
import org.gristle.adventOfCode.utilities.groupValues
import kotlin.math.abs

// not refactored
class Y2017D20(input: String) : Day {

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

    override fun part1(): Int {
        val selectParticles = particles.filter {
            it.a.manhattanDistance() == particles.first().a.manhattanDistance()
        }
        val offset = selectParticles.maxOf { it.stableTime() }
        return selectParticles
            .maxByOrNull { it.particleAt(offset).p.manhattanDistance() }
            ?.number
            ?: throw Exception("selectParticles has no elements")
    }

    override fun part2(): Int {
        val collisionSequence = generateSequence(particles) { last ->
            val collisionsRemoved = last
                .groupBy { it.p }
                .filter { it.value.size == 1 }
                .map { it.value.first() }
            collisionsRemoved.map { it.particleAt(1) }
        }

        return collisionSequence
            .take(1000)
            .last()
            .size
    }
}

fun main() = Day.runDay(Y2017D20::class)

//    Class creation: 55ms
//    Part 1: 308 (4ms)
//    Part 2: 504 (580ms)
//    Total time: 640ms