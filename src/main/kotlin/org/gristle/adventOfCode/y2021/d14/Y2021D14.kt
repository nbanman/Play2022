package org.gristle.adventOfCode.y2021.d14

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.blankSplit
import org.gristle.adventOfCode.utilities.minMax

private typealias PropagationRules = Map<String, Pair<String, String>>
private typealias ProteinPairs = Map<String, Long>

class Y2021D14(input: String) : Day {
    private val proteinPairs: ProteinPairs
    private val firstAndLast: String
    private val rules: PropagationRules
    init {

        // Intermediate parsing step for defining above values, splitting the template input from the rules input
        val (polymerTemplate, rulesInput) = input.blankSplit()

        // Initial polymer state represented as a map of pairs of proteins along with the number of times the pair
        // exists in the String.    
        proteinPairs = buildMap {
            polymerTemplate.windowed(2).forEach { key ->
                this[key] = (this[key] ?: 0L) + 1 // bumps count
            }
        }

        // The main algorithm does not properly count proteins that are at the ends of the polymer, so those proteins
        // need to be retained.
        firstAndLast = "${polymerTemplate.first()}${polymerTemplate.last()}"

        // Propagation rules. The solving algorithm counts number of neighboring proteins.
        // Thus, the insertion rule is represented by a pair of strings. "AB => C" becomes "AC" to "CB"
        rules = buildMap {
            rulesInput // start with input string
                .filter { it.isLetter() } // discard formatting and only look at "proteins"
                .toList() // convert to List<Char> for nifty destructuring
                .chunked(3) // groups of 3 proteins
                .forEach { (a, b, c) -> put("$a$b", "$a$c" to "$c$b") } // assign values to map
        }
    }

    /**
     * Runs a single step, returning a map with updated pair counts.
     */
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

        // Runs the stepping process n number of times.
        val steppedPairs = (1..steps).fold(proteinPairs) { pairs, _ -> pairs.step() }

        // Count number of proteins by iterating through the pairs and adding the pair amount for each time the 
        // protein appears. Note that the value stored is a double count because the same protein is counted in the
        // first position and then in the last position!
        val proteinCounts = buildMap {
            for ((proteins, amt) in steppedPairs) {
                for (protein in proteins) {
                    this[protein] = (this[protein] ?: 0L) + amt
                }
            }

            // Proteins at the very end are not double-counted, so bump the count for these to make it consistent. 
            for (protein in firstAndLast) {
                this[protein] = (this[protein] ?: 0L) + 1L
            }
        }

        // Grab the highest and lowest values, return the difference (divided by 2 to adjust the double-count).
        return proteinCounts.values.minMax().let { (min, max) -> (max - min) / 2 }
    }

    override fun part1() = solve(10)

    override fun part2() = solve(40)
}

fun main() = Day.runDay(14, 2021, Y2021D14::class)

//    Class creation: 10ms
//    Part 1: 3555 (4ms)
//    Part 2: 4439442043739 (3ms)
//    Total time: 18ms