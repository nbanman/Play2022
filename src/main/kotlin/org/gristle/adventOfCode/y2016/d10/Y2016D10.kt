package org.gristle.adventOfCode.y2016.d10

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.minMax

class Y2016D10(input: String) : Day {

    data class Bot(val name: Int, val high: Gift, val low: Gift, var storage: Int = -1) {
        companion object {
            var responsibleBot: Bot? = null
        }

        fun take(value: Int, bots: List<Bot>, respTrack: Set<Int>, output: MutableList<Output>): Boolean {
            return if (storage == -1) {
                storage = value
                false
            } else {
                val (lowValue, highValue) = minMax(storage, value)
                val isResp = setOf(storage, value) == respTrack
                storage = -1
                if (low.isBot) {
                    bots
                        .find { it.name == low.name }
                        ?.take(lowValue, bots, respTrack, output)
                        ?: throw Exception("low bot name not found in list of bots")
                } else {
                    output.add(Output(lowValue, low.name))
                }
                if (high.isBot) {
                    bots
                        .find { it.name == high.name }
                        ?.take(highValue, bots, respTrack, output)
                        ?: throw Exception("high bot name not found in list of bots")
                } else {
                    output.add(Output(highValue, high.name))
                }
                if (isResp) responsibleBot = this
                isResp
            }
        }
    }

    data class Output(val value: Int, val bin: Int)

    data class Gift(val name: Int, val isBot: Boolean)

    private val respTrack = setOf(61, 17)

    private val botDirections = """bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)""".toRegex()

    private val chipAssignments = """value (\d+) goes to bot (\d+)""".toRegex()

    val output = mutableListOf<Output>()

    // make bots
    private val bots = input
        .groupValues(botDirections)
        .map {
            Bot(
                it[0].toInt(),
                Gift(it[4].toInt(), it[3] == "bot"),
                Gift(it[2].toInt(), it[1] == "bot"),
            )
        }

    init {
        input
            .groupValues(chipAssignments)
            .forEach { gv ->
                val bot = bots.find { it.name == gv[1].toInt() }
                    ?: throw Exception("Chip assignment bot name not found in list of bots.")
                bot.take(gv[0].toInt(), bots, respTrack, output)
            }
    }

    override fun part1() = Bot.responsibleBot?.name

    override fun part2() = output.filter { it.bin < 3 }.map { it.value }.reduce { acc, i -> acc * i }
}

fun main() = Day.runDay(Y2016D10::class)

//    Class creation: 29ms
//    Part 1: 101 (0ms)
//    Part 2: 37789 (0ms)
//    Total time: 29ms