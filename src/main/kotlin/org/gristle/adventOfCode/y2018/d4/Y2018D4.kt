package org.gristle.adventOfCode.y2018.d4

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.transpose

class Y2018D4(input: String) : Day {

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

    private val guards = buildMap<String, MutableList<MutableList<Boolean>>> {
        var minutes = MutableList(60) { false }
        logs.forEach { log ->
            when (log.event) {
                "wakes up" -> minutes.wake(log.minute)
                "falls asleep" -> minutes.sleep(log.minute)
                else -> {
                    val guard = getOrPut(log.event) { mutableListOf() }
                    guard.add(MutableList(60) { false })
                    minutes = guard.last()
                }
            }
        }
    } as Map<String, List<List<Boolean>>>

    val getTimeList: (Map.Entry<String, List<List<Boolean>>>) -> IndexedValue<List<Boolean>> = { guard ->
        guard
            .value
            .transpose()
            .withIndex()
            .maxByOrNull { (_, minute) -> minute.count { it } }
            ?: throw Exception("Map entry has no value")
    }

    override fun part1(): Int {
        val sleepiest = guards.entries
            .maxByOrNull { guard -> guard.value.sumOf { day -> day.count { it } } }
            ?: throw Exception("Map entry has no value")
        val guardId = sleepiest.key.drop(1).toInt()
        val sleepiestTimes = getTimeList(sleepiest)
        return guardId * sleepiestTimes.index
    }

    override fun part2() = guards
        .entries
        .map { guard ->
            val minuteMap = getTimeList(guard)
            guard.key to minuteMap
        }.maxByOrNull { (_, minMax) -> minMax.value.count { it } }
        ?.let { it.first.drop(1).toInt() * it.second.index }
}

fun main() = Day.runDay(4, 2018, Y2018D4::class)

//    Class creation: 43ms
//    Part 1: 19025 (7ms)
//    Part 2: 23776 (4ms)
//    Total time: 55ms