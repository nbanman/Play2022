package org.gristle.adventOfCode

import org.gristle.adventOfCode.utilities.*
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
//            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            var part2: String? = null
            val timer = Stopwatch(true)
            val c = constructor.call(input) as Day
            println("Class creation: ${timer.lap()}ms")
            val part1 = c.part1().toString()
            println("\tPart 1: $part1 (${timer.lap()}ms)")
            if (day != 25) {
                part2 = c.part2().toString()
                println("\tPart 2: $part2 (${timer.lap()}ms)")
            }
            println("Total time: ${timer.elapsed()}ms")

//            clipboard.setContents(StringSelection(part1), null)
//            clipboard.getContents(this)
//            if (part2 != null) clipboard.setContents(StringSelection(part2), null)
        }

        fun <T : Any> runDay(
            kClass: KClass<T>,
            sampleInput: List<String>,
            displayInput: Boolean = false,
        ) {
            val constructor = kClass.constructors.first()
            val (year, day) = kClass.simpleName?.getIntList()
                ?: throw IllegalArgumentException("Class does not have a name")
            println("[$year Day $day]")
            sampleInput.forEachIndexed { index, sample ->
                val trimmedSample = sample.trimEnd()
                print("${index + 1}:")
                val inputString = if (displayInput) " $trimmedSample\t" else "\t"
                print(inputString)
                val c = constructor.call(trimmedSample) as Day
                println("Part 1: ${c.part1()}\tPart 2: ${c.part2()}")
            }
        }

        fun <T : Any> testPart(
            kClass: KClass<T>,
            part: Int,
            sampleInput: List<Pair<String, String>>,
            displayInput: Boolean = false,
        ) {
            val constructor = kClass.constructors.first()
            val (year, day) = kClass.simpleName?.getIntList()
                ?: throw IllegalArgumentException("Class does not have a name")
            println("[$year Day $day]")
            sampleInput.forEachIndexed { index, (sample, answer) ->
                val trimmedSample = sample.trimEnd()
                print("${index + 1}:\t")
                val c = constructor.call(trimmedSample) as Day
                val result =
                    when {
                        part == 1 -> c.part1()
                        part == 2 && day != 25 -> c.part2()
                        else -> throw IllegalArgumentException("Invalid part number: $part")
                    }.convertToString()
                if (result == answer) {
                    print("SUCCESS\t")
                } else {
                    print("FAILURE\t")
                }
                print("$result ($answer)")
                if (displayInput) println("\t$trimmedSample") else println()
            }
        }

        fun <T : Any> testDay(
            kClass: KClass<T>,
            sampleInput: String? = null,
            skipPartOne: Boolean = false,
            skipPartTwo: Boolean = false
        ): Pair<String, String> {
            val constructor = kClass.constructors.first()
            val (year, day) = kClass.simpleName?.getIntList()
                ?: throw IllegalArgumentException("Class does not have a name")
            val input = sampleInput ?: getInput(day, year)
            val c = constructor.call(input) as Day
            val part1 = if (skipPartOne) "skipped" else c.part1().toString()
            val part2 = if (skipPartTwo || day == 25) "skipped" else c.part2().toString()
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
            val timer = Stopwatch(true, TimeUnits.US)
            var c = constructor.call(input) as Day
            println("${kClass.simpleName} Part 1\n")
            val p1Average = benchmark(warmups, iterations, timer, c::part1)
            c = constructor.call(input) as Day
            println("\n${kClass.simpleName} Part 2\n")
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