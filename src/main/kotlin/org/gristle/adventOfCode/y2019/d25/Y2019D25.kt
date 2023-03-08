package org.gristle.adventOfCode.y2019.d25

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.getLongList
import org.gristle.adventOfCode.y2019.IntCode.ICSave
import org.gristle.adventOfCode.y2019.IntCode.IntCode
import java.io.IOException
import java.util.*

private typealias Readout = Triple<String, List<String>, List<String>>

class Y2019D25(private val input: String) : Day {

    fun play() {
        val initialState = input.getLongList()
        val output: Deque<Long> = LinkedList()
        val toComp: Deque<Long> = LinkedList()
        val intCode = IntCode("A", initialState, null, toComp, output)
        intCode.run()
        val saves = mutableMapOf<String, ICSave>()
        while (true) {
            output.print()
            val input = readLine() ?: throw IOException("Failed to read from console")

            val firstWord = input.takeWhile { it != ' ' }
            val remainder = input.dropWhile { it != ' ' }

            val expandedInput = when (firstWord) {
                "q" -> "quit"
                "n" -> "north"
                "s" -> "south"
                "e" -> "east"
                "w" -> "west"
                "t" -> "take"
                "d" -> "drop"
                "i" -> "inv"
                else -> firstWord
            } + remainder

            if (expandedInput.contains("save ")) {
                saves[expandedInput.takeLastWhile { it != ' ' }] = intCode.save()
                continue
            }

            if (expandedInput.contains("load ")) {
                intCode.restore(saves.getValue(expandedInput.takeLastWhile { it != ' ' }))
                continue
            }

            if (expandedInput.contains("quit")) break

            toComp.addAll(expandedInput.map { it.code.toLong() })
            toComp.add(10L)
            intCode.run(10_000)
        }
    }

    private val locationRx = Regex("""== ((?:\w+ ?)+) ==""")
    private val doorsRx = Regex("""Doors here lead:\n((?:- (?:\w+)\n)+)""")
    private val itemizedRx = Regex("""Items here:\n((?:- (?:[a-z ]+)\n)+)""")
    private val splitRx = Regex("""\w+(?: \w+)?""")

    private fun Deque<Long>.parse(clear: Boolean = true): Readout {
        val output = read()
        if (clear) clear()
        val location = locationRx.find(output)?.groupValues?.get(1)
            ?: throw IllegalArgumentException("output does not contain location")
        val doors = doorsRx
            .find(output)
            ?.groupValues
            ?.get(1)
            ?.let { s -> splitRx.findAll(s).map { it.value }.toList() }
            ?: throw IllegalArgumentException("output does not contain doors")
        val items = itemizedRx
            .find(output)
            ?.groupValues
            ?.get(1)
            ?.let { s -> splitRx.findAll(s).map { it.value }.toList() }
            ?: emptyList()
        return Readout(location, doors, items)
    }

    private fun Deque<Long>.print() {
        val output = buildString {
            this@print.forEach {
                append(it.toInt().toChar())
            }
        }
        clear()
    }

    private fun String.toCode() = map { it.code.toLong() }

    private fun Iterable<Long>.read() = map { it.toInt().toChar() }.joinToString("")

    private val reverse = mapOf(
        "north" to "south",
        "south" to "north",
        "east" to "west",
        "west" to "east",
    )

    private fun execute(command: String, intCode: IntCode, toComp: Deque<Long>) {
        toComp.addAll(command.toCode())
        toComp.add(10L)
        intCode.run(100_000)
    }

    private fun explore(
        endCondition: Boolean,
        command: String,
        previousLocation: String,
        intCode: IntCode,
        toComp: Deque<Long>,
        output: Deque<Long>
    ): String {
        execute(command, intCode, toComp)
        val (currentLocation, doors, items) = output.parse()

        // returns early if hits pressure plate
        if (previousLocation == currentLocation) {
            return if (endCondition) command else ""
        }

        // picks up items, undos action if fatal
        if (!endCondition) items.forEach { item ->
            val save = intCode.save()
            execute("take $item", intCode, toComp)
            output.clear()
            execute("", intCode, toComp)
            if (!output.read().contains("Unrecognized")) {
                intCode.restore(save)
            }
        }

        // moves to next spot
        doors.filter { it != reverse[command] }.forEach { door ->
            val endCommand = explore(endCondition, door, currentLocation, intCode, toComp, output)
            if (endCommand.isNotBlank()) return endCommand
        }

        // moves back out
        if (command.isNotEmpty()) {
            execute(reverse.getValue(command), intCode, toComp)
            output.clear()
        }

        return ""
    }

    private fun getPasscode(
        inventory: List<String>,
        direction: String,
        index: Int,
        intCode: IntCode,
        toComp: Deque<Long>,
        output: Deque<Long>,
    ): String {
        val step = step(direction, intCode, toComp, output)

        if (step != "light") return step

        for (i in index until inventory.size) {
            execute("take ${inventory[i]}", intCode, toComp)
            output.clear()
            val innerStep = getPasscode(inventory, direction, i + 1, intCode, toComp, output)
            if (innerStep == "heavy") {
                execute("drop ${inventory[i]}", intCode, toComp)
                output.clear()
            } else if (innerStep != "light") {
                return innerStep
            }
        }

        return "heavy"
    }

    private fun step(direction: String, intCode: IntCode, toComp: Deque<Long>, output: Deque<Long>): String {
        toComp.addAll(direction.toCode())
        toComp.add(10L)
        intCode.run()
        val report = output.read()
        output.clear()
        return if ("lighter" in report) {
            "heavy"
        } else if ("heavier" in report) {
            "light"
        } else report.getInts().first().toString()
    }

    override fun part1(): String {

        val initialState = input.getLongList()
        val output: Deque<Long> = java.util.ArrayDeque()
        val toComp: Deque<Long> = java.util.ArrayDeque()
        val intCode = IntCode("A", initialState, null, toComp, output)

        intCode.run()

        val outputCopy = (output as java.util.ArrayDeque).clone()

        explore(false, "", "Outer Space", intCode, toComp, output)

        output.addAll(outputCopy)

        val plateDirection = explore(
            true,
            "",
            "Outer Space",
            intCode, toComp, output
        )

        execute("inv", intCode, toComp)

        val inventory = output
            .read()
            .lineSequence()
            .filter { it.isNotBlank() && it[0] == '-' }
            .map { line -> line.drop(2) }
            .toList()

        inventory.forEach { item ->
            execute("drop $item", intCode, toComp)
        }

        return getPasscode(inventory, plateDirection, 0, intCode, toComp, output)
    }

    override fun part2() = "Merry Xmas!!!"
}

fun main() {
//    Y2019D25(readRawInput("y2019/d25")).play()
    Day.runDay(Y2019D25::class)
}

//    Class creation: 15ms
//    Part 1: 16810049 (304ms)
//    Total time: 319ms
