package org.gristle.adventOfCode.y2023.d12

import org.gristle.adventOfCode.Day
import kotlin.math.min

class Y2023D12(input: String) : Day {

    /**
     * 'conditionsIndex' tracks which "block" of '#' and '?' characters, with no '.' characters. The SpringRow
     * class holds these blocks in a list.
     * 'place' is the index of substring of the block
     * 'damageReportIndex' tracks which series of broken springs to assign from the list kept by SpringRow class
     */
    data class State(
        val conditionsIndex: Int,
        val place: Int,
        val damageReportIndex: Int,
    )

    data class SpringRow(val conditions: List<String>, val damageReport: List<Int>) {

        private val cache = mutableMapOf<State, Long>()

        fun arrangements(s: State = State(0, 0, 0)): Long {
            val cachedAnswer = cache[s]
            if (cachedAnswer != null) {
                return cachedAnswer
            }
            return cache.getOrPut(s) {
                val block = conditions.getOrNull(s.conditionsIndex)?.substring(s.place) ?: ""
                val fulfillment = damageReport.getOrNull(s.damageReportIndex) ?: 0

                // Base case. Takes states at the end of the line and returns 1 if valid, 0 if invalid
                // Valid states are those with no remaining '#' in the current or any future blocks, and that
                // have filled all the damaged spring requirements
                if (fulfillment == 0) {
                    return@getOrPut if ('#' !in block && conditions.drop(s.conditionsIndex + 1).all { '#' !in it }) {
                        1
                    } else {
                        0
                    }
                } else if (block == "") {
                    return@getOrPut 0
                }

                // Otherwise, create new states
                val brokenIndex = block.indexOf('#').let { if (it == -1) Int.MAX_VALUE else it }
                val newStates = min(block.length - fulfillment, brokenIndex) + 1

                val subArrangements = (fulfillment until fulfillment + newStates)
                    .filter { nextIndex ->
                        // the character after the block MUST be a '?', otherwise the block would continue
                        nextIndex >= block.length || block[nextIndex] == '?'
                    }.sumOf { nextIndex ->
                        // if there is still some block left, stay on block (can always skip on next pass)
                        if (nextIndex + 1 <= block.lastIndex) {
                            val newState = State(
                                conditionsIndex = s.conditionsIndex,
                                place = s.place + nextIndex + 1,
                                damageReportIndex = s.damageReportIndex + 1
                            )
                            arrangements(newState)
                        } else { // otherwise go to next block
                            val newState = State(
                                conditionsIndex = s.conditionsIndex + 1,
                                place = 0,
                                damageReportIndex = s.damageReportIndex + 1
                            )
                            arrangements(newState)
                        }
                    }

                val skipArrangement = if (block.all { it == '?' }) {
                    val newState = State(s.conditionsIndex + 1, 0, s.damageReportIndex)
                    arrangements(newState)
                } else {
                    0
                }
                return@getOrPut subArrangements + skipArrangement
            }
        }
    }

    private val springReports = input.lines().map { line ->
        val (conditions, damageReportStr) = line.split(' ')
        val damageReport: List<Int> = damageReportStr.split(',').map(String::toInt)
        conditions to damageReport
    }

    override fun part1(): Long {
        val springRows = springReports.map { (conditionsStr, damageReport) ->
            SpringRow(
                groupRx.findAll(conditionsStr).map(MatchResult::value).toList(),
                damageReport
            )
        }
        return springRows.sumOf { springRow -> springRow.arrangements() }
    }

    override fun part2(): Long {
        val springRows = springReports.map { (conditionsStr, damageReport) ->
            val expandedConditions = (1..5).joinToString("?") { conditionsStr }
            val expandedDamageReport = (1..5).flatMap { damageReport }
            SpringRow(
                groupRx.findAll(expandedConditions).map(MatchResult::value).toList(),
                expandedDamageReport
            )
        }
        return springRows.sumOf { springRow -> springRow.arrangements() }
    }

    companion object {
        val groupRx = Regex("[?#]+")
    }
}

fun main() = Day.runDay(Y2023D12::class)

//    Class creation: 16ms
//    Part 1: 7344 (28ms)
//    Part 2: 1088006519007 (129ms)
//    Total time: 174ms

@Suppress("unused")
private val sampleInput = listOf(
    """???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
"""
)