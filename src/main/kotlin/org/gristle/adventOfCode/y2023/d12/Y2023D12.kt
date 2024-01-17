package org.gristle.adventOfCode.y2023.d12

import org.gristle.adventOfCode.Day

class Y2023D12(input: String) : Day {

    data class State(val conditionsIndex: Int, val damageReportIndex: Int)

    data class SpringRow(val conditions: String, val damageReport: List<Int>) {
        // cache used for DP function 'arrangements,' below. 
        private val cache = mutableMapOf<State, Long>()
        
        // DP function that uses state objects to delve deeper and deeper into the string using DFS, until a base case 
        // is found. Cache is updated along the way, so that subsequent dives down the string become instant as soon as
        // there is any repetition of remaining state.
        fun arrangements(s: State = State(0, 0)): Long = cache.getOrPut(s) {
            // the # of consecutive broken springs in the damage report that we will try to place along the row. If 
            // every broken spring in the damage report has been assigned, return 0.
            val fulfillment = damageReport.getOrNull(s.damageReportIndex) ?: 0

            // Base case. Takes states that have assigned broken springs corresponding to the entire damage report and 
            // returns 1 if valid, 0 if invalid. Valid states are those with no remaining '#' in the string.
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

    // intermediate parsing step. Each part parses the reports differently.
    private val springReports: List<Pair<String, List<Int>>> = input.lines().map { line ->
        val (conditions, damageReportStr) = line.split(' ')
        val damageReport: List<Int> = damageReportStr.split(',').map(String::toInt)
        conditions to damageReport
    }
    
    // parses the reports to rows, then sums the number of arrangements possible in each row
    private inline fun solve(springRow: (String, List<Int>) -> SpringRow): Long = springReports
        .sumOf { (conditions, damageReport) -> springRow(conditions, damageReport).arrangements() }

    override fun part1(): Long = solve { conditions, damageReport -> SpringRow(conditions, damageReport) } 
    
    override fun part2(): Long = solve { conditions, damageReport ->
        val expandedConditions = List(5) { conditions }.joinToString("?")
        val expandedDamageReport = List(5) { damageReport }.flatten()
        SpringRow(expandedConditions, expandedDamageReport)
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