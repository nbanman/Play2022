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
     * Stores the range to execute the particular instruction and executes the instruction. Comes in three 
     * different flavors that execute different functions: On, Off, and Toggle. 
     */
    sealed class Instruction(private val topLeft: Coord, private val bottomRight: Coord) {
        /**
         * Returns the specific function from the suite of functions provided for each part, corresponding to
         * whether the instruction is On, Off, or Toggle.
         */
        abstract fun getAdjuster(lightAdjustment: LightAdjustment): Adjuster

        /**
         * Adjusts the light field in accordance with the instruction and the given range.
         */
        fun execute(lights: MutableList<Int>, lightAdjustment: LightAdjustment) {
            val adjust = getAdjuster(lightAdjustment)
            Coord.forRectangle(topLeft, bottomRight) { x, y -> adjust(lights, x, y) }
        }
        
        companion object {
            /**
             * Provides the appropriate instruction object.
             */
            fun of(groupValues: List<String>): Instruction {
                val coords = groupValues.drop(1).map { it.toInt() }
                val topLeft = Coord(coords[0], coords[1])
                val bottomRight = Coord(coords[2], coords[3])

                return when (groupValues[0]) {
                    "turn on" -> On(topLeft, bottomRight)
                    "turn off" -> Off(topLeft, bottomRight)
                    else -> Toggle(topLeft, bottomRight)
                }
            }
        }
    }

    class On(topLeft: Coord, bottomRight: Coord) : Instruction(topLeft, bottomRight) {
        override fun getAdjuster(lightAdjustment: LightAdjustment) = lightAdjustment::on
    }
    class Off(topLeft: Coord, bottomRight: Coord) : Instruction(topLeft, bottomRight) {
        override fun getAdjuster(lightAdjustment: LightAdjustment) = lightAdjustment::off
    }
    class Toggle(topLeft: Coord, bottomRight: Coord) : Instruction(topLeft, bottomRight) {
        override fun getAdjuster(lightAdjustment: LightAdjustment) = lightAdjustment::toggle
    }

    /**
     * Parses input into List of Instructions
     */
    private val instructions = readRawInput("y2015/d6")
        .groupValues("""(turn (?:on|off)|toggle) (\d+),(\d+) through (\d+),(\d+)""")
        .map { Instruction.of(it) }

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
            override fun on(lights: MutableList<Int>, x: Int, y: Int) { 
                lights[y * 1000 + x] = 1 
            }
            override fun off(lights: MutableList<Int>, x: Int, y: Int) { 
                lights[y * 1000 + x] = 0 
            }
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
            override fun on(lights: MutableList<Int>, x: Int, y: Int) { 
                lights[y * 1000 + x]++ 
            }
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