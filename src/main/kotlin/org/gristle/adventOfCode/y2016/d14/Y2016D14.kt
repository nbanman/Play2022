package org.gristle.adventOfCode.y2016.d14

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Md5.toHex
import java.security.MessageDigest

class Y2016D14(private val salt: String) : Day {

    // utility function for accessing the indexes of 5-strings in an IntArray
    private fun Char.hexIndex(): Int = if (isDigit()) digitToInt() else this - 'W'

    // Regexes used to find 3- and 5-strings in hashes
    private val threeRx = Regex("""([0-9a-f])\1{2,}""")
    private val fiveRx = Regex("""([0-9a-f])\1{4,}""")

    @OptIn(FlowPreview::class)
    private fun solve(hashing: (String) -> String): Int = runBlocking {

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

        val chunk = 128

        /**
         * Returns a Flow performing the hashing function $chunk number of times. Uses async/await to do tasks
         * asynchronously, but then collect them all to deliver to the Flow in sequential order.
         */
        suspend fun md5s(n: Int): Flow<String> = withContext(Dispatchers.Default) {
            (n * chunk until n * chunk + chunk)
                .map { seed ->
                    async {
                        hashing(salt + seed)
                    }
                }.awaitAll()
                .asFlow()
        }

        // Flow starting with increasing index, generating a hash based on that and the salt. For each hash,
        // record any 5-string in the fives with the current index. Add the 3-string value to the rolling list.
        // When 3-string values start rolling off, check fives to see if that value has shown up as a five. If
        // so, add it to the list of keys. Keep going until the 64th key is found.

        generateSequence(0) { it + 1 } // Sequence starting with increasing index...
            .asFlow() // turn into Flow
            .map(::md5s)
            .flattenConcat()
            .withIndex()
            .onEach { (n, md5) ->
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
            .collect() // launches Flow

        keys.last()
    }

    private fun md5(s: String): String = MessageDigest
        .getInstance("MD5")!!
        .apply { update(s.toByteArray()) }
        .digest()
        .toHex()

    override fun part1() = solve(::md5)

    // function to perform stretched MD5 per part 2 instructions
    private fun stretchedMd5(s: String): String {
        val digest = MessageDigest.getInstance("MD5")!!
        return (1..2017).fold(s) { acc, _ -> digest.apply { update(acc.toByteArray()) }.digest().toHex() }
    }

    override fun part2() = solve(::stretchedMd5)
}

fun main() = Day.runDay(Y2016D14::class)

//    Class creation: 2ms
//    Part 1: 18626 (280ms)
//    Part 2: 20092 (2109ms)
//    Total time: 2392ms

//    Benchmarking Y2016D14 Part 1
//    
//    Warm-up 1: 267 ms/op
//    Iteration 1: 136 ms/op
//    Iteration 2: 127 ms/op
//    Iteration 3: 115 ms/op
//    Iteration 4: 80 ms/op
//    Iteration 5: 66 ms/op
//    
//    104.8 ms/op [Average]
//    
//    Warm-up 1: 1708 ms/op
//    Iteration 1: 1519 ms/op
//    Iteration 2: 1548 ms/op
//    Iteration 3: 1519 ms/op
//    Iteration 4: 1519 ms/op
//    Iteration 5: 1518 ms/op
//    
//    1524.6 ms/op [Average]
//    Parts 1 and 2: 1628 ms/op [Average]