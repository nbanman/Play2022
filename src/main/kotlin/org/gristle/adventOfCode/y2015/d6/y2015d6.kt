package org.gristle.adventOfCode.y2015.d6

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput
import kotlin.math.max

/**
 * A function that adjusts a light in a particular location.
 */
typealias Adjuster = (MutableList<Int>, Int, Int) -> Unit
object Y2015D6 {
    
    /**
     * Defines the suite of  three functions that alter the lights. Defined as an interface because the functions  
     * for part 1 and part 2 work differently, so will plug in different implementations for each part. They work
     * on a MutableList so all return Unit.
     */
    interface LightAdjustment {
        fun on(lights: MutableList<Int>, x: Int, y: Int)
        fun off(lights: MutableList<Int>, x: Int, y: Int)
        fun toggle(lights: MutableList<Int>, x: Int, y: Int)
    }

    /**
     * Each instruction is either ON, OFF, or TOGGLE. This class provides the appropriate adjustment function from
     * the LightAdjustment suite.
     */
    enum class Mode { 
        ON { override fun getAdjuster(lightAdjustment: LightAdjustment) = lightAdjustment::on }, 
        OFF { override fun getAdjuster(lightAdjustment: LightAdjustment) = lightAdjustment::off }, 
        TOGGLE { override fun getAdjuster(lightAdjustment: LightAdjustment) = lightAdjustment::toggle };
        
        abstract fun getAdjuster(lightAdjustment: LightAdjustment): Adjuster
    }
  
    data class Instruction(val mode: Mode, val topLeft: Coord, val bottomRight: Coord) {
        fun execute(lights: MutableList<Int>, lightAdjustment: LightAdjustment) {
            val adjust = mode.getAdjuster(lightAdjustment)
            for (y in topLeft.y..bottomRight.y) {
                for (x in topLeft.x..bottomRight.x) { 
                    adjust(lights, x, y)
                }
            }
        }
    }

    // Parsing...
    private val input = readRawInput("y2015/d6")

    private val pattern = """(turn (?:on|off)|toggle) (\d+),(\d+) through (\d+),(\d+)""".toRegex()

    private val instructions = input
        .groupValues(pattern)
        .map { gv ->
            val coords = gv.drop(1).map { it.toInt() }
            Instruction(
                when (gv[0]) {
                    "turn on" -> Mode.ON
                    "turn off" -> Mode.OFF
                    else -> Mode.TOGGLE
                },
                Coord(coords[0], coords[1]),
                Coord(coords[2], coords[3])
            )
        }

    /**
     * Algo for solving both parts. Creates a light field as a mutable list. Executes each instruction on the
     * light field. Returns the sum of the brightness.
     */
    fun solve(lightAdjustment: LightAdjustment): Int {
        val lights = MutableList(1_000_000) { 0 }
        instructions.forEach { it.execute(lights, lightAdjustment) }
        return lights.sum()
    }

    /**
     * Runs solve algo with part 1-specific adjustment functions.
     */
    fun part1() = solve(
        object : LightAdjustment {
            override fun on(lights: MutableList<Int>, x: Int, y: Int) { lights[y * 1000 + x] = 1 }
            override fun off(lights: MutableList<Int>, x: Int, y: Int) { lights[y * 1000 + x] = 0 }
            override fun toggle(lights: MutableList<Int>, x: Int, y: Int) {
                (y * 1000 + x).let { newCoord -> lights[newCoord] = if (lights[newCoord] == 1) 0 else 1 }
            }
        }
    )

    /**
     * Runs solve algo with part 2-specific adjustment functions.
     */
    fun part2() = solve(
        object : LightAdjustment {
            override fun on(lights: MutableList<Int>, x: Int, y: Int) { lights[y * 1000 + x]++ }
            override fun off(lights: MutableList<Int>, x: Int, y: Int) { 
                lights[y * 1000 + x] = max(0, lights[y * 1000 + x] - 1) 
            }
            override fun toggle(lights: MutableList<Int>, x: Int, y: Int) {
                lights[y * 1000 + x] += 2
            }
        }
    )
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D6.part1()} (${elapsedTime(time)}ms)") // 569999
    time = System.nanoTime()
    println("Part 2: ${Y2015D6.part2()} (${elapsedTime(time)}ms)") // 17836115
}