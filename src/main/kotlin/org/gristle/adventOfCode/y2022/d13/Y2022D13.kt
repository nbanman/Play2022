package org.gristle.adventOfCode.y2022.d13

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput

class Y2022D13(input: String) {
    sealed class PData : Comparable<PData> {
        data class Value(val value: Int) : PData() {

            fun toPacket() = Packet(listOf(this))
            override fun compareTo(other: PData): Int {
                fun Int.coerce() = coerceIn(-1..1)
                return when (other) {
                    is Value -> (this.value - other.value).coerce()
                    else -> this.toPacket().compareTo(other)
                }
            }
        }

        data class Packet(val list: List<PData>) : PData() {
            override fun compareTo(other: PData): Int {
                when (other) {
                    is Packet -> {
                        (this.list zip other.list).forEach { (leftItem, rightItem) ->
                            val answer = leftItem.compareTo(rightItem)
                            if (answer != 0) return answer
                        }
                        return (this.list.size - other.list.size).coerce()
                    }

                    is Value -> {
                        return this.compareTo(other.toPacket())
                    }
                }
            }
        }

        companion object {
            fun Int.coerce() = coerceIn(-1..1)

            private val regex = Regex("""\[|]|\d+""")

            fun of(line: String): Packet {
                val parser: Iterator<String> = regex.findAll(line).map(MatchResult::value).iterator()
                fun makePacket(): Packet {
                    val list = mutableListOf<PData>()
                    while (parser.hasNext()) {
                        when (val next = parser.next()) {
                            "[" -> list.add(makePacket())
                            "]" -> break
                            else -> list.add(Value(next.toInt()))
                        }
                    }
                    return Packet(list)
                }
                return makePacket()
            }
        }
    }

    private val packets = input
        .lines()
        .filter(String::isNotBlank)
        .map(PData::of)

    fun part1(): Int = packets
        .chunked(2)
        .mapIndexed { index, value ->
            val (left, right) = value
            if (left < right) (index + 1) else 0
        }.sum()

    fun part2(): Int {
        val dividerPackets = listOf(PData.of("[[2]]"), PData.of("[[6]]")).sorted()
        return dividerPackets
            .mapIndexed { index, packet -> packets.count { packet > it } + 1 + index }
            .reduce(Int::times)
    }
}

fun main() {
    val input = getInput(13, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D13(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 5506
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 21756
    println("Total time: ${timer.elapsed()}ms")
}