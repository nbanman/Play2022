package org.gristle.adventOfCode.y2021.d16

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.print
import org.gristle.adventOfCode.utilities.readRawInput

object Y2021D16 {
    private val input = readRawInput("y2021/d16")

    sealed class Packet(val version: Int, val typeId: Int) {

        abstract fun versionSum(): Int

        abstract fun value(): Long

        class Literal(version: Int, typeId: Int, private val literalValue: Long) : Packet(version, typeId) {

            override fun versionSum() = version

            override fun value() = literalValue

            override fun toString(): String {
                return "Literal(version=$version, typeId=$typeId, literalValue=$literalValue)"
            }
        }

        class Operator(version: Int, typeId: Int, private val subPackets: List<Packet>) : Packet(version, typeId) {

            override fun value(): Long = when (typeId) {
                0 -> subPackets.sumOf { it.value() }
                1 -> subPackets.map { it.value() }.reduce { acc, l -> acc * l }
                2 -> subPackets.minOf { it.value() }
                3 -> subPackets.maxOf { it.value() }
                5 -> if (subPackets.first().value() > subPackets.last().value()) 1 else 0
                6 -> if (subPackets.first().value() < subPackets.last().value()) 1 else 0
                7 -> if (subPackets.first().value() == subPackets.last().value()) 1 else 0
                else -> 0L
            }

            override fun versionSum() = version + subPackets.sumOf { it.versionSum() }

            override fun toString(): String {
                return "Literal(version=$version, typeId=$typeId, value=${value()}, subPackets=$subPackets)"
            }
        }

        companion object {

            fun parse(bp: BitProvider, verbose: Boolean = false): Packet {
                verbose.print("Making new packet... BitProvider: ${bp.size}")
                val version = bp.getBitInt(3)
                val typeId = bp.getBitInt(3)
                verbose.print("Version: $version, typeId: $typeId")
                if (typeId == 4) {
                    val literalValue = buildString {
                        while (bp.getBitInt(1) == 1) {
                            append(bp.getBitString(4))
                        }
                        append(bp.getBitString(4))
                    }.let { java.lang.Long.parseLong(it, 2) }
                    verbose.print("literalValue: $literalValue\nLiteral packet created!")
                    return Literal(version, typeId, literalValue)
                }
                val lengthId = bp.getBitInt(1)
                val length = bp.getBitInt(if (lengthId == 0) 15 else 11)
                verbose.print("lengthId: $lengthId, length: $length")
                val subPackets = when (lengthId) {
                    0 -> {
                        val subBp = BitProvider(bp.getBitString(length))
                        verbose.print("Creating subBitProvider of size ${subBp.size}")
                        mutableListOf<Packet>().apply {
                            while (subBp.isNotEmpty()) {
                                verbose.print("Creating sub-packet with subBitProvider size ${subBp.size}")
                                add(parse(subBp, verbose))
                            }
                        }
                    }
                    else -> {
                        List(length) { i ->
                            verbose.print("Creating sub-packet ${i + 1} of $length with bp size ${bp.size}")
                            parse(bp, verbose)
                        }
                    }
                }
                verbose.print("Finished creating Operator!")
                return Operator(version, typeId, subPackets)
            }
        }
    }

    class BitProvider(private val binary: String) {

        val size: Int
            get() = binary.length - parser

        fun isEmpty() = size <= 0

        fun isNotEmpty() = !isEmpty()

        private var parser = 0

        fun getBitString(n: Int): String {
            require(parser + n <= binary.length)
            return binary.substring(parser, parser + n).also { parser += n }
        }

        fun getBitInt(n: Int): Int {
            require(parser + n <= binary.length && n < 32)
            return binary
                .substring(parser, n + parser)
                .let { Integer.parseInt(it, 2) }
                .also { parser += n }
        }

        companion object {
            private val conversion = mapOf(
                '0' to "0000",
                '1' to "0001",
                '2' to "0010",
                '3' to "0011",
                '4' to "0100",
                '5' to "0101",
                '6' to "0110",
                '7' to "0111",
                '8' to "1000",
                '9' to "1001",
                'A' to "1010",
                'B' to "1011",
                'C' to "1100",
                'D' to "1101",
                'E' to "1110",
                'F' to "1111",
            )

            fun fromHex(hex: String): BitProvider {
                return BitProvider(hex.map { conversion[it]!! }.joinToString(""))
            }

        }
    }

    private val bp = BitProvider.fromHex(input)

    private val packet = Packet.parse(bp, false)

    fun part1() = packet.versionSum()

    fun part2() = packet.value()
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D16.part1()} (${elapsedTime(time)}ms)") // 979
    time = System.nanoTime()
    println("Part 2: ${Y2021D16.part2()} (${elapsedTime(time)}ms)") // 277110354175
}