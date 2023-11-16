package org.gristle.adventOfCode.y2021.d16

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.print

class Y2021D16b(input: String) : Day {

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

            override fun versionSum() = version + subPackets.sumOf(Packet::versionSum)

            override fun toString(): String {
                return "Operator(version=$version, typeId=$typeId, value=${value()}, subPackets=$subPackets)"
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
                        val subBp = bp.getSubBP(length)
                        verbose.print("Creating subBitProvider of size ${subBp.size}")
                        buildList {
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

    class BitProvider(private val binary: BooleanArray) {

        val size: Int
            get() = binary.size - parser

        fun isEmpty() = size <= 0

        fun isNotEmpty() = !isEmpty()

        private var parser = 0

        fun getBitString(n: Int): String = getBitInt(n)
            .toString(2)
            .padStart(4, '0')

        fun getSubBP(n: Int): BitProvider {
            val subBP = binary.copyOfRange(parser, parser + n)
            parser += n
            return BitProvider(subBP)
        }

        fun getBitInt(n: Int): Int {
            require(parser + n <= binary.size && n < 32)
            val bitInt = (parser until parser + n).foldIndexed(0) { index, acc, i ->
                val bit = if (binary[i]) 1 else 0
                acc + (bit shl (n - index - 1))
            }
            parser += n
            return bitInt
        }

        companion object {
            private fun hexToBits(hex: String): BooleanArray {
                val ba = BooleanArray(hex.length * 4)

                hex.forEachIndexed { hexIndex, c ->
                    val num = c.code.let { code -> if (code < 65) code - 48 else code - 55 }
                    generateSequence(num) { it / 2 }
                        .take(4)
                        .forEachIndexed { index, i -> ba[hexIndex * 4 + 3 - index] = i and 1 == 1 }
                }

                return ba
            }

            fun fromHex(hex: String) = BitProvider(hexToBits(hex))
        }
    }

    private val packet = Packet.parse(BitProvider.fromHex(input))

    override fun part1(): Int {
        return packet.versionSum()
    }

    override fun part2() = packet.value()
}

fun main() = Day.runDay(Y2021D16b::class)

//    Class creation: 21ms
//    Part 1: 979 (0ms)
//    Part 2: 277110354175 (1ms)
//    Total time: 23ms