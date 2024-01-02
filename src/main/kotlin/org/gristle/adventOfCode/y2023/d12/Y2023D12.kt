package org.gristle.adventOfCode.y2023.d12

import org.gristle.adventOfCode.Day

class Y2023D12(input: String) : Day {

    data class State(val conditionsIndex: Int, val damageReportIndex: Int)

    data class SpringRow(val conditions: String, val damageReport: List<Int>) {
        private val cache = mutableMapOf<State, Long>()

        fun arrangements(s: State = State(0, 0)): Long = cache.getOrPut(s) {
            // the # of consecutive broken springs in the damage report that we try to place along the row
            val fulfillment = damageReport.getOrNull(s.damageReportIndex) ?: 0

            // Base case. Takes states that have fulfilled the entire damage report and returns 1 if valid, 
            // 0 if invalid. Valid states are those with no remaining '#' in the current or any future blocks, 
            // and that have filled all the damaged spring requirements
            if (fulfillment == 0) {
                val damagedSpringRemaining = (s.conditionsIndex..conditions.lastIndex).any { conditions[it] == '#' }
                return@getOrPut if (damagedSpringRemaining) 0 else 1
            }
            
            // Otherwise, we go recursive by trying to fit the fulfillment in every place along the block
            // This starts as a sequence of indexes, from 0 until the length of the block minus the fulfillment size
            // (to account for the size of the fulfillment itself in the string).
            (s.conditionsIndex..conditions.length - fulfillment)
                .asSequence()
                // stop the sequence if a '#' precedes the index b/c '#' cannot be skipped
                .takeWhile { index -> conditions.getOrNull(index - 1) != '#' }
                .filter { index ->
                    // filter out invalid placements, in cascading fashion
                    // if the placement includes a '.', invalid b/c '.' means not broken
                    // if the placement has no part of the string after it, valid b/c nothing else to consider
                    // if the character following the placement is '#', invalid b/c that extra '#' would overfulfill
                    // otherwise valid
                    when {
                        '.' in conditions.substring(index, index + fulfillment) -> false
                        index + fulfillment == conditions.length -> true
                        conditions[index + fulfillment] == '#' -> false
                        else -> true
                    }
                }.sumOf { index ->
                    val newState = State(
                        index + fulfillment + 1,
                        s.damageReportIndex + 1
                    )
                    arrangements(newState)
                }
        }
    }

    // parsing
    private val springReports: List<Pair<String, List<Int>>> = input.lines().map { line ->
        val (conditions, damageReportStr) = line.split(' ')
        val damageReport: List<Int> = damageReportStr.split(',').map(String::toInt)
        conditions to damageReport
    }

    override fun part1(): Long {
        val springRows = springReports.map { (conditions, damageReport) ->
            SpringRow(conditions, damageReport)
        }
        return springRows.sumOf { springRow -> springRow.arrangements() }
    }

    override fun part2(): Long {
        val springRows = springReports.map { (conditions, damageReport) ->
            val expandedConditions = List(5) {conditions}.joinToString("?") 
            val expandedDamageReport = List(5) { damageReport }.flatten()
            SpringRow(expandedConditions, expandedDamageReport)
        }
        return springRows.sumOf { springRow -> springRow.arrangements() }
    }
}

fun main() = Day.runDay(Y2023D12::class)

//    Class creation: 16ms
//    Part 1: 7344 (19ms)
//    Part 2: 1088006519007 (157ms)
//    Total time: 193ms

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