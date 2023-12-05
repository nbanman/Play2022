package org.gristle.adventOfCode.y2023.d5

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.blankSplit
import org.gristle.adventOfCode.utilities.getLongList

class Y2023D5(input: String) : Day {

    data class Listing(val sourceStart: Long, val destinationStart: Long, val length: Long) {
        val sourceRange = sourceStart until sourceStart + length
        val destinationRange = destinationStart until destinationStart + length
    }

    private val seeds: List<Long>

    private val conversions: List<List<Listing>>

    init {
        val chunked = input.blankSplit()
        seeds = chunked[0].getLongList()
        conversions = chunked.drop(1)
            .map { listing ->
                listing
                    .getLongList()
                    .chunked(3) { (destinationStart, sourceStart, length) ->
                        Listing(sourceStart, destinationStart, length)
                    }
                    .sortedBy { it.sourceStart }
            }
    }

    private fun List<Listing>.convert(
        source: Long,
        from: Listing.() -> LongRange,
        to: Listing.() -> LongRange
    ): Long =
        firstOrNull { listing -> source in listing.from() }
            ?.let { listing -> listing.to().first + source - listing.from().first }
            ?: source

    private fun List<List<Listing>>.convertAll(
        source: Long,
        from: Listing.() -> LongRange,
        to: Listing.() -> LongRange
    ): Long = fold(source) { acc, listings -> listings.convert(acc, from, to) }

    override fun part1() =
        seeds.minOf { conversions.convertAll(it, Listing::sourceRange, Listing::destinationRange) }

    override fun part2(): Any {
        val seedRanges: List<LongRange> = seeds
            .chunked(2) { (start, length) -> start until start + length }
            .sortedBy { it.first }
        val testRange: LongRange = conversions
            .last()
            .minBy { it.destinationStart }
            .destinationRange
        return testRange
            .asSequence()
            .map { it to conversions.reversed().convertAll(it, Listing::destinationRange, Listing::sourceRange) }
            .filter { (_, source) ->
                seedRanges.any {
                    source in it
                }
            }.first()
            .first
    }
}

fun main() = Day.runDay(Y2023D5::class)

//    Class creation: 11ms
//    Part 1: 379811651 (1ms)
//    Part 2: 27992443 (9549ms)
//    Total time: 9562ms

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