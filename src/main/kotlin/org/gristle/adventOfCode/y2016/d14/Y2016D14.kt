package org.gristle.adventOfCode.y2016.d14

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.md5

class Y2016D14(private val salt: String) : Day {

    // function to perform stretched MD5 per part 2 instructions
    private fun String.stretchedMd5() = (1..2017).fold(this) { acc, _ -> acc.md5() }

    // utility function for accessing the indexes of 5-strings in an IntArray
    private fun Char.hexIndex(): Int = if (isDigit()) digitToInt() else this - 'W'

    // Regexes used to find 3- and 5-strings in hashes
    private val threeRx = Regex("""([0-9a-f])\1{2,}""")
    private val fiveRx = Regex("""([0-9a-f])\1{4,}""")

    private fun solve(hashing: (String) -> String): Int {

        // for each hex value 0-f, store index of last time 5-string appeared
        val fives = IntArray(16) { -1 }

        // mutable list of validated keys
        val keys = ArrayList<Int>(70)

        // rolling list of 1,000 of the 3-string value of hashes. returns 'X' if no 3-string in hash
        val threes = ArrayDeque<Char>(1000)

        // adds a 3-string value to the rolling list, returning the index of the value that rolled off if that
        // index has been validated
        fun ArrayDeque<Char>.addRolling(three: Char, index: Int): Int? {

            // add value
            addLast(three)

            // do nothing if list is not full
            if (size <= 1000) return null

            // if full, start rolling off
            val evaluate = removeFirst()
            if (evaluate == 'X') return null
            val evaluateIndex = index - 1000

            // check fives to see if any of the next 1,000 hashes has a 5-string matching the rolling off 3-string
            if (fives[evaluate.hexIndex()] !in evaluateIndex + 1..index) return null
            return evaluateIndex
        }

        // Sequence starting with increasing index, generating a hash based on that and the salt. For each hash,
        // record any 5-string in the fives with the current index. Add the 3-string value to the rolling list.
        // When 3-string values start rolling off, check fives to see if that value has shown up as a five. If
        // so, add it to the list of keys. Keep going until the 64th key is found.
        generateSequence(0) { it + 1 } // Sequence starting with increasing index... 
            .onEach { n ->
                val md5: String = hashing(salt + n) // ...generating a hash based on that and the salt.

                // For each hash, record any 5-string in the fives with the current index.
                fiveRx
                    .findAll(md5)
                    .forEach { fives[it.groupValues[1][0].hexIndex()] = n }

                // Add the 3-string value to the rolling list.
                val three = threeRx.find(md5)?.value?.get(0) ?: 'X'
                threes
                    .addRolling(three, n) // When 3-string values start rolling off, check fives... 
                    ?.let { key -> keys.add(key) } // ...If so, add it to the list of keys.
            }.takeWhile { keys.size < 64 } // Keep going until the 64th key is found.
            .last()

        return keys.last()
    }

    override fun part1() = solve(String::md5)

    override fun part2() = solve { it.stretchedMd5() }
}

fun main() = Day.runDay(Y2016D14::class)

//    Class creation: 2ms
//    Part 1: 18626 (95ms)
//    Part 2: 20092 (11337ms)
//    Total time: 11435ms
