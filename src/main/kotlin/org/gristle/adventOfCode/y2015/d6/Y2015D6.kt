package org.gristle.adventOfCode.y2015.d6

import org.gristle.adventOfCode.utilities.*
import kotlin.math.max

class Y2015D6(input: String) {
    
    /**
     * Defines the suite of  three functions that alter the lights. Defined as an interface because the functions  
     * for part 1 and part 2 work differently, so will plug in different implementations for each part. They work
     * on a MutableList so all return Unit.
     */
    interface LightAdjustment {
        fun on(lights: MutableGrid<Int>, location: Coord)
        fun off(lights: MutableGrid<Int>, location: Coord)
        fun toggle(lights: MutableGrid<Int>, location: Coord)
    }

    /**
     * Stores the range to execute the particular instruction and executes the instruction. Comes in three 
     * different flavors that execute different functions: On, Off, and Toggle. 
     */
    class Instruction(
        private val topLeft: Coord,
        private val bottomRight: Coord,
        private val getAdjuster: (LightAdjustment) -> (MutableGrid<Int>, Coord) -> Unit
    ) {

        /**
         * Adjusts the light field in accordance with the instruction and the given range.
         */
        fun execute(lights: MutableGrid<Int>, lightAdjustment: LightAdjustment) {
            val adjust = getAdjuster(lightAdjustment)
            Coord.forRectangle(topLeft, bottomRight) { location -> adjust(lights, location) }
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
                    "turn on" -> Instruction(topLeft, bottomRight) { it::on }
                    "turn off" -> Instruction(topLeft, bottomRight) { it::off }
                    else -> Instruction(topLeft, bottomRight) { it::toggle }
                }
            }
        }
    }

    /**
     * Parses input into List of Instructions
     */
    private val instructions = input
        .groupValues("""(turn (?:on|off)|toggle) (\d+),(\d+) through (\d+),(\d+)""")
        .map { Instruction.of(it) }

    /**
     * Algo for solving both parts. Creates a light field as a mutable list. Executes each instruction on the
     * light field. Returns the sum of the brightness.
     */
    fun solve(lightAdjustment: LightAdjustment): Int {
        val lights = MutableGrid(1000, 1000) { 0 }
        instructions.forEach { it.execute(lights, lightAdjustment) }
        return lights.sum()
    }

    /**
     * Runs solve algo with part 1-specific adjustment functions.
     */
    fun part1() = solve(
        object : LightAdjustment {
            override fun on(lights: MutableGrid<Int>, location: Coord) {
                lights[location] = 1
            }

            override fun off(lights: MutableGrid<Int>, location: Coord) {
                lights[location] = 0
            }

            override fun toggle(lights: MutableGrid<Int>, location: Coord) {
                lights[location] = if (lights[location] == 1) 0 else 1
            }
        }
    )

    /**
     * Runs solve algo with part 2-specific adjustment functions.
     */
    fun part2() = solve(
        object : LightAdjustment {
            override fun on(lights: MutableGrid<Int>, location: Coord) {
                lights[location]++
            }

            override fun off(lights: MutableGrid<Int>, location: Coord) {
                lights[location] = max(0, lights[location] - 1)
            }

            override fun toggle(lights: MutableGrid<Int>, location: Coord) {
                lights[location] += 2
            }
        }
    )
}

fun main() {
    var time = System.nanoTime()
    val c = Y2015D6(readRawInput("y2015/d6"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 569999
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 17836115
}