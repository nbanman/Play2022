package org.gristle.adventOfCode

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.getIntList
import kotlin.reflect.KClass

interface Day {
    fun part1(): Any?
    fun part2(): Any?

    companion object {
        fun <T : Any> runDay(
            kClass: KClass<T>,
            sampleInput: String? = null,
        ) {
            val constructor = kClass.constructors.first()
            val (year, day) = kClass.simpleName?.getIntList()
                ?: throw IllegalArgumentException("Class does not have a name")
            println("[$year Day $day]")
            val input = sampleInput ?: getInput(day, year)
            val timer = Stopwatch(true)
            val c = constructor.call(input) as Day
            println("Class creation: ${timer.lap()}ms")
            println("\tPart 1: ${c.part1()} (${timer.lap()}ms)")
            if (day != 25) println("\tPart 2: ${c.part2()} (${timer.lap()}ms)")
            println("Total time: ${timer.elapsed()}ms")
        }

        fun <T : Any> runDay(
            kClass: KClass<T>,
            sampleInput: List<String>,
            omitInput: Boolean = false,
        ) {
            val constructor = kClass.constructors.first()
            val (year, day) = kClass.simpleName?.getIntList()
                ?: throw IllegalArgumentException("Class does not have a name")
            println("[$year Day $day]")
            sampleInput.forEachIndexed { index, sample ->
                print("${index + 1}:")
                val inputString = if (omitInput) "\t" else " $sample\t"
                print(inputString)
                val c = constructor.call(sample) as Day
                println("Part 1: ${c.part1()}\tPart 2: ${c.part2()}")
            }
        }

        fun <T : Any> testDay(
            kClass: KClass<T>,
            sampleInput: String? = null,
            skipPartOne: Boolean = false,
            skipPartTwo: Boolean = false
        ): Any {
            val constructor = kClass.constructors.first()
            val (year, day) = kClass.simpleName?.getIntList()
                ?: throw IllegalArgumentException("Class does not have a name")
            val input = sampleInput ?: getInput(day, year)
            val c = constructor.call(input) as Day
            val part1 = if (skipPartOne) false else c.part1()
            val part2 = if (skipPartTwo) false else c.part2()
            return part1 to part2
        }
    }
}