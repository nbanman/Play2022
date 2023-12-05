package org.gristle.adventOfCode.y2023.d5

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.blankSplit
import org.gristle.adventOfCode.utilities.getLongList
import kotlin.math.min

class Y2023D5(input: String) : Day {

    data class Listing(val sourceStart: Long, val destinationStart: Long, val length: Long) {
        private val sourceEnd = sourceStart + length - 1

        // returns up to 3 sub-ranges, applying conversion where appropriate
        fun splitAndSend(source: LongRange): List<LongRange> = buildList {

            // first is anything before the Listing; no offset
            if (source.first < sourceStart) {
                add(source.first..min(source.last, sourceStart))
            }

            // second is any overlap with the Listing; offset by difference between destination and source
            val offset = destinationStart - sourceStart
            if (source.last >= sourceStart && source.first <= sourceEnd) {
                val newFirst = if (isEmpty()) source.first else sourceStart
                add(newFirst + offset..min(source.last, sourceEnd) + offset)
            }

            // third is anything after the Listing; no offset
            when {
                isEmpty() -> add(source)
                last().last == source.last -> {}
                last().last == source.last + offset -> {}
                else -> add(last().last + 1 - offset..source.last)
            }
        }
    }

    private val seeds: List<Long>
    private val conversions: List<List<Listing>>

    init {
        val stanzas = input.blankSplit().map { it.getLongList() }
        seeds = stanzas[0]
        conversions = stanzas
            .drop(1)
            .map { mapNumbers ->
                mapNumbers
                    .chunked(3) { (destinationStart, sourceStart, length) ->
                        Listing(sourceStart, destinationStart, length)
                    }.sortedBy(Listing::sourceStart)
            }
    }

    fun solve(seedRanges: List<LongRange>): Long = seedRanges
        .minOf { seedRange -> // for each seedRange, find the smallest end number

            // feed a list of ranges through a gauntlet of conversion stages. the list of ranges will grow each step
            // as each conversion handles different parts of the range differently.
            val subRanges = conversions.fold(listOf(seedRange)) { ranges, listings ->
                ranges.flatMap { range ->
                    // fold constantly breaks down the last range in the list, as it runs through the listings
                    listings.fold(mutableListOf(range)) { subRanges, listing ->
                        if (subRanges.last().last == range.last) {
                            subRanges.addAll(listing.splitAndSend(subRanges.removeLast()))
                        }
                        subRanges
                    }
                }
            }

            // we only care about the lowest number of all these subRanges
            subRanges.minOf { it.first }
        }

    override fun part1() = solve(seeds.map { it..it })

    override fun part2() = solve(seeds.chunked(2) { (start, length) -> start until start + length })
}

fun main() = Day.runDay(Y2023D5::class)

//    Class creation: 14ms
//    Part 1: 379811651 (4ms)
//    Part 2: 27992443 (4ms)
//    Total time: 23ms

@Suppress("unused")
private val sampleInput = listOf(
    """seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4""",
)