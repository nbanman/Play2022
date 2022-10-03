package org.gristle.adventOfCode.y2018.d4

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.transpose

class Y2018D4(input: String) {

    data class LogEntry(
        val month: Int,
        val day: Int,
        val hour: Int,
        val minute: Int,
        val event: String
    ) : Comparable<LogEntry> {
        override fun compareTo(other: LogEntry) =
            compareBy<LogEntry> { it.month }
                .thenBy { it.day }
                .thenBy { it.hour }
                .thenBy { it.minute }
                .compare(this, other)
    }

    private val pattern = """\[1518-(\d\d)-(\d\d) (\d\d):(\d\d)] (?:Guard )?(#\d+|falls asleep|wakes up)""".toRegex()

    private val logs = input
        .groupValues(pattern)
        .map { gv ->
            val ints = gv.dropLast(1).map { it.toInt() }
            LogEntry(ints[0], ints[1], ints[2], ints[3], gv.last())
        }.sorted()

    private fun MutableList<Boolean>.sleep(minute: Int) {
        for (i in minute..lastIndex) this[i] = true
    }

    private fun MutableList<Boolean>.wake(minute: Int) {
        for (i in minute..lastIndex) this[i] = false
    }

    private val guards = mutableMapOf<String, MutableList<MutableList<Boolean>>>().apply {
        var minutes = MutableList(60) { false }
        logs.forEach { log ->
            when (log.event) {
                "wakes up" -> minutes.wake(log.minute)
                "falls asleep" -> minutes.sleep(log.minute)
                else -> {
                    val guard = computeIfAbsent(log.event) { mutableListOf() }
                    guard.add(MutableList(60) { false })
                    minutes = guard.last()
                }
            }
        }
    }

    fun part1(): Int {
        val sleepiest = guards.entries
            .maxByOrNull { guard -> guard.value.sumOf { day -> day.count { it } } }!!
        val guardId = sleepiest.key.drop(1).toInt()
        val sleepiestTimes = sleepiest
            .value
            .transpose()
            .withIndex()
            .maxByOrNull { (_, minute) -> minute.count { it } }!!
        return guardId * sleepiestTimes.index
    }

    fun part2() = guards.entries.map { guard ->
        val minuteMap = guard.value
            .transpose()
            .withIndex()
            .maxByOrNull { (_, minute) -> minute.count { it } }!!
        guard.key to minuteMap
    }.maxByOrNull { (_, minMax) -> minMax.value.count { it } }!!
        .let { it.first.drop(1).toInt() * it.second.index }}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D4(readRawInput("y2018/d4"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 19025
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 23776
}