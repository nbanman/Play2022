package org.gristle.adventOfCode.y2022.d6

import org.gristle.adventOfCode.Day

class Y2022D6(private val input: String) : Day {
    // Pretty one-liner (that I came up with) is 4x slower
    private fun slowSolve(n: Int) = n + input.windowed(n).indexOfFirst { it.toSet().size == n }

    // Alexander af Trolle's brilliant solution, modified to use an IntArray for kicks.
    private fun solve(n: Int): Int {
        // tracks the index of the last time the character was encountered
        val indexMap = IntArray(26)
        // tracks the index of the last time a first character of a duplicate was encountered
        var duplicateIndex = 0
        var index = 0
        return input.indexOfFirst { c ->
            // update the index in the map and grab the previous index for that character 
            val lastSeen = indexMap[c - 'a']
            indexMap[c - 'a'] = index
            duplicateIndex = duplicateIndex.coerceAtLeast(lastSeen) // update duplicateIndex
            // if the last duplicate's first character index is further than n characters away, predicate matched 
            index++ - duplicateIndex >= n
        } + 1
    }

    override fun part1() = solve(4)
    override fun part2() = solve(14)
}

fun main() = Day.runDay(Y2022D6::class)

//    Class creation: 2ms
//    Part 1: 1361 (4ms)
//    Part 2: 3263 (2ms)
//    Total time: 9ms

private val p1Test = listOf(
    """mjqjpqmgbljsphdztnvjfqwrcgsmlb""" to "7",
    """bvwbjplbgvbhsrlpgdmjqwftvncz""" to "5",
    """nppdvjthqldpwncqszvftbrmjlhg""" to "6",
    """nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg""" to "10",
    """zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw""" to "11",
)

private val p2Test = listOf(
    """mjqjpqmgbljsphdztnvjfqwrcgsmlb""" to "19",
    """bvwbjplbgvbhsrlpgdmjqwftvncz""" to "23",
    """nppdvjthqldpwncqszvftbrmjlhg""" to "23",
    """nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg""" to "29",
    """zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw""" to "26",
)