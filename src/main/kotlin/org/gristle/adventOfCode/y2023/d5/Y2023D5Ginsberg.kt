package org.gristle.adventOfCode.y2023.d5

import org.gristle.adventOfCode.Day

/*
 * Copyoutgoing (c) 2023 by Todd Ginsberg
 */

/**
 * Advent of Code 2023, Day 5 - If You Give A Seed A Fertilizer
 * Problem Description: http://adventofcode.com/2023/day/5
 * Blog Post/Commentary: https://todd.ginsberg.com/post/advent-of-code/2023/day5/
 */

class Y2023D5Ginsberg(rawInput: String) : Day {

    val input = rawInput.lines()
    private val seedsPart1: List<Long> = parsePart1Seeds(input)
    private val seedsPart2: Set<LongRange> = parsePart2Seeds(input)
    private val ranges = parseRanges(input)

    override fun part1(): Long =
        seedsPart1.minOf { seed ->
            ranges.fold(seed) { acc, ranges ->
                ranges.firstOrNull { acc in it }?.translate(acc) ?: acc
            }
        }

    override fun part2(): Long {
        val rangesReversed = ranges.map { range -> range.map { it.flip() } }.reversed()
        return generateSequence(0L, Long::inc).first { location ->
            val seed = rangesReversed.fold(location) { acc, ranges ->
                ranges.firstOrNull { acc in it }?.translate(acc) ?: acc
            }
            seedsPart2.any { seedRange -> seed in seedRange }
        }
    }

    private fun parseRanges(input: List<String>): List<Set<RangePair>> =
        input.drop(2).joinToString("\n").split("\n\n").map {
            it.split("\n").drop(1).map { line -> RangePair.of(line) }.toSet()
        }

    private fun parsePart1Seeds(input: List<String>): List<Long> =
        input.first().substringAfter(":").trim().split(" ").map { it.toLong() }

    @OptIn(ExperimentalStdlibApi::class)
    private fun parsePart2Seeds(input: List<String>): Set<LongRange> =
        input.first().substringAfter(":").trim().split(" ")
            .map { it.toLong() }.chunked(2).map {
                it.first()..<it.first() + it.last()
            }.toSet()

    private data class RangePair(
        private val source: LongRange,
        private val destination: LongRange
    ) {
        fun flip(): RangePair =
            RangePair(destination, source)

        fun translate(num: Long): Long =
            destination.first + (num - source.first)

        operator fun contains(num: Long): Boolean =
            num in source

        companion object {
            @OptIn(ExperimentalStdlibApi::class)
            fun of(row: String): RangePair {
                val parts = row.split(" ").map { it.toLong() }
                return RangePair(
                    parts[1]..<(parts[1] + parts[2]),
                    parts[0]..<(parts[0] + parts[2])
                )
            }
        }
    }
}

fun main() = Day.benchmarkDay(Y2023D5Ginsberg::class)

//    Class creation: 11ms
//    Part 1: 379811651 (4ms)
//    Part 2: 27992443 (4ms)
//    Total time: 20ms

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