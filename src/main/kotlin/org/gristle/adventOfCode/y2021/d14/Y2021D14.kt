package org.gristle.adventOfCode.y2021.d14

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.minMax

private typealias ProteinPairs = Map<String, Long>

class Y2021D14(input: String) : Day {

    // propagation rules. Defined in init block because there is an intermediate parsing step for both rules and 
    // allProteins that does not need to be saved. The solving algorithm counts number of neighboring proteins.
    // Thus, the insertion rule is represented by a pair of strings. "AB => C" becomes "AC" to "CB"
    private val rules: Map<String, Pair<String, String>>

    // List of all the proteins for the algorithm to iterate through
    private val allProteins: List<Char>

    init {
        // intermediate parsing step drops the first line and only considers letters
        val letters = input.dropWhile { it != '\n' }.filter { it.isLetter() }.toList()

        rules = buildMap {
            letters
                .chunked(3)
                .forEach { (a, b, c) -> put("$a$b", "$a$c" to "$c$b") }
        }

        allProteins = letters.distinct()
    }

    // Initial polymer state represented as a map of pairs of proteins along with the number of times the pair
    // exists in the String. Defined in init block because there is an intermediate parsing step for both proteinPairs
    // and firstAndLast that does not need to be saved.
    private val proteinPairs: ProteinPairs

    // The main algorithm does not properly count proteins that are at the ends of the polymer, so those proteins
    // need to be retained.
    private val firstAndLast: String

    init {
        // intermediate parsing step, the first line of the input
        val polymerTemplate = input.takeWhile { it != '\n' }

        proteinPairs = buildMap {
            polymerTemplate.windowed(2).forEach { key ->
                this[key] = (this[key] ?: 0L) + 1 // bumps count
            }
        }

        firstAndLast = "${polymerTemplate.first()}${polymerTemplate.last()}"
    }

    // runs a single step, returning a map with updated pair counts
    private fun ProteinPairs.step(): Map<String, Long> {
        val newPairs = mutableMapOf<String, Long>()
        forEach { (polyPair, amt) ->
            val (a, b) = rules.getValue(polyPair)
            newPairs[a] = (newPairs[a] ?: 0L) + amt
            newPairs[b] = (newPairs[b] ?: 0L) + amt
        }
        return newPairs
    }

    /**
     * The protein expands exponentially, so rather than try to store the protein as a string, the algorithm
     * updates a map that keeps track of how many times a pair has been encountered. Then each step, that pair
     * repropagates using the propagation rules, and the resulting pairs' amounts are increased by the amount of
     * the original pair. This double-counts the number of all the proteins except the ones at the very beginning and
     * end, so extra counts are made for the end proteins and then the result is divided by 2. This is the number of
     * total proteins. Then the answer is the most common protein minus the least common protein.
     */
    private fun solve(steps: Int): Long {
        val steppedPairs = (1..steps).fold(proteinPairs) { pairs, _ -> pairs.step() }

        val proteinCount = allProteins.map { protein ->
            val count = steppedPairs
                .filter { it.key.contains(protein) }
                .map { if (it.key[0] == it.key[1]) it.value * 2 else it.value }
                .sum()
            val modifier = firstAndLast.count { it == protein }
            (count + modifier) / 2
        }

        return proteinCount.minMax().let { (min, max) -> max - min }
    }

    override fun part1() = solve(10)

    override fun part2() = solve(40)
}

fun main() = Day.runDay(14, 2021, Y2021D14::class)

//    Class creation: 15ms
//    Part 1: 3555 (6ms)
//    Part 2: 4439442043739 (2ms)
//    Total time: 24ms