package org.gristle.adventOfCode.y2022.d13

import org.gristle.adventOfCode.Day

class Y2022D13(input: String) : Day {
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

    override fun part1(): Int = packets
        .chunked(2)
        .mapIndexed { index, value ->
            val (left, right) = value
            if (left < right) (index + 1) else 0
        }.sum()

    override fun part2(): Int {
        val dividerPackets = listOf(PData.of("[[2]]"), PData.of("[[6]]")).sorted()
        return dividerPackets
            .mapIndexed { index, packet -> packets.count { packet > it } + 1 + index }
            .reduce(Int::times)
    }
}

fun main() = Day.runDay(Y2022D13::class) // 5506, 21756