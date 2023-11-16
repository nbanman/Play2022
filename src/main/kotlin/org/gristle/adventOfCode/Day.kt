package org.gristle.adventOfCode

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.TimeUnits
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.getIntList
import kotlin.reflect.KClass
import kotlin.reflect.KFunction0

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
            val part2 = if (skipPartTwo || day == 25) false else c.part2()
            return part1 to part2
        }

        fun <T : Any> benchmarkDay(
            kClass: KClass<T>,
            sampleInput: String? = null,
            warmups: Int = 1,
            iterations: Int = 5
        ) {
            val constructor = kClass.constructors.first()
            val (year, day) = kClass.simpleName?.getIntList()
                ?: throw IllegalArgumentException("Class does not have a name")
            val input = sampleInput ?: getInput(day, year)
            val c = constructor.call(input) as Day
            println("${kClass.simpleName} Part 1\n")
            val timer = Stopwatch(true, TimeUnits.US)
            val p1Average = benchmark(warmups, iterations, timer, c::part1)
            println("\n${kClass.simpleName} Part 1\n")
            val p2Average = benchmark(warmups, iterations, timer, c::part2)
            println("\nParts 1 and 2: ${p1Average + p2Average} us/op [Average]")
        }

        private fun benchmark(
            warmups: Int,
            iterations: Int,
            timer: Stopwatch,
            part: KFunction0<Any?>
        ): Long {
            for (warmup in 1..warmups) {
                print("Warm-up $warmup: ")
                timer.lap()
                part()
                println("${timer.lap()} us/op")
            }
            val times = (1..iterations)
                .map { iteration ->
                    print("Iteration $iteration: ")
                    timer.lap()
                    part()
                    timer.lap().also { println("$it us/op") }
                }
            val average = times.average()
            println("\n$average us/op [Average]")
            return average.toLong()
        }
    }
}