package org.gristle.adventOfCode

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import kotlin.reflect.KClass

interface Day {
    fun part1(): Any?
    fun part2(): Any?

    companion object {
        fun <T : Any> runDay(
            day: Int,
            year: Int,
            kClass: KClass<T>? = null,
            sampleInput: String? = null,
        ) {
            val constructor = if (kClass == null) {
                val name = "org.gristle.adventOfCode.y$year.d$day.Y${year}D$day"
                Class.forName(name).kotlin.constructors.first()
            } else {
                kClass.constructors.first()
            }
            val timer = Stopwatch(true)
            println("[$year Day $day]")
            val input = sampleInput ?: getInput(day, year)
            val c = constructor.call(input) as Day
            println("Class creation: ${timer.lap()}ms")
            println("\tPart 1: ${c.part1()} (${timer.lap()}ms)")
            if (day != 25) println("\tPart 2: ${c.part2()} (${timer.lap()}ms)")
            println("Total time: ${timer.elapsed()}ms")
        }
    }
}