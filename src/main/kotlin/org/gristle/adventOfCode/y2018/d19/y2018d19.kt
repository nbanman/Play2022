package org.gristle.adventOfCode.y2018.d19

import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.y2018.d19.Y2018D19.store

enum class Ops(val fn: (reg: List<Long>, p: Int, a: Int, b: Int, c: Int) -> List<Long>) {
    ADDR( { reg, p, a, b, c -> reg.store(p, c, reg[a] + reg[b]) } ),
    ADDI( { reg, p, a, b, c -> reg.store(p, c, reg[a] + b) } ),
    MULR( { reg, p, a, b, c -> reg.store(p, c, reg[a] * reg[b]) } ),
    MULI( { reg, p, a, b, c -> reg.store(p, c, reg[a] * b) }),
    BANR( { reg, p, a, b, c -> reg.store(p, c, reg[a] and reg[b]) } ),
    BANI( { reg, p, a, b, c -> reg.store(p, c, reg[a] and b.toLong()) } ),
    BORR( { reg, p, a, b, c -> reg.store(p, c, reg[a] or reg[b]) } ),
    BORI( { reg, p, a, b, c -> reg.store(p, c, reg[a] or b.toLong()) } ),
    SETR( { reg, p, a, _, c -> reg.store(p, c, reg[a]) } ),
    SETI( { reg, p, a, _, c -> reg.store(p, c, a.toLong()) } ),
    GTIR( { reg, p, a, b, c -> reg.store(p, c, if (a > reg[b]) 1 else 0) } ),
    GTRI( { reg, p, a, b, c -> reg.store(p, c, if (reg[a] > b) 1 else 0) } ),
    GTRR( { reg, p, a, b, c -> reg.store(p, c, if (reg[a] > reg[b]) 1 else 0) } ),
    EQIR( { reg, p, a, b, c -> reg.store(p, c, if (a.toLong() == reg[b]) 1 else 0) } ),
    EQRI( { reg, p, a, b, c -> reg.store(p, c, if (reg[a] == b.toLong()) 1 else 0) } ),
    EQRR( { reg, p, a, b, c -> reg.store(p, c, if (reg[a] == reg[b]) 1 else 0) } );
    
    companion object {
        fun from(s: String) = when (s) {
            "addr" -> Ops.ADDR
            "addi" -> Ops.ADDI
            "mulr" -> Ops.MULR
            "muli" -> Ops.MULI
            "banr" -> Ops.BANR
            "bani" -> Ops.BANI
            "borr" -> Ops.BORR
            "bori" -> Ops.BORI
            "setr" -> Ops.SETR
            "seti" -> Ops.SETI
            "gtir" -> Ops.GTIR
            "gtri" -> Ops.GTRI
            "gtrr" -> Ops.GTRR
            "eqir" -> Ops.EQIR
            "eqri" -> Ops.EQRI
            else -> Ops.EQRR
        }
    }
}

data class Command(val op: Ops, val p: Int, val a: Int, val b: Int, val c: Int) {
    fun execute(reg: List<Long>) = op.fn(reg, p, a, b, c)
}

object Y2018D19 {
    private val data = readInput("y2018/d19")

    fun List<Long>.store(pointer: Int, location: Int, result: Long): List<Long> {
        val newReg = mapIndexed { index, i ->
            if (index == location) result else i
        }
        return newReg.mapIndexed { index, l ->  if (index == pointer) l + 1 else l }
    }

    fun part1(): Long {
        val p = data.first().takeLast(1).toInt()
        val commands = data
            .drop(1)
            .map { line ->
                line
                    .split(" ")
                    .let { fields ->
                        val ops = Ops.from(fields[0])
                        Command(ops, p, fields[1].toInt(), fields[2].toInt(), fields[3].toInt())
                    }
            }

        var register = List(6) { 0L }
        while (register[p] in commands.indices) {
            register = commands[register[p].toInt()].execute(register)
        }
        return register[0]
    }

    fun part2(): Int {
//     Loop is such that R3 starts as 1, R5 goes up by 1. R2 is R3 * R5. When R2 equals 105k, R0+= R3 and R3++, R5 resets
//     If R2 goes past 105K w/o equaling it (not divisible), then R3++ and R5 resets w/o RO going up. Thus RO adds all
//     the numbers that divide evenly into 105k.
        val targetNum = 10_551_370
        return (1..targetNum).fold(0) { acc, i ->
            if (targetNum % i == 0) acc + i else acc
        }
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2018D19.part1()} (${elapsedTime(time)}ms)") // 1764
    time = System.nanoTime()
    println("Part 2: ${Y2018D19.part2()} (${elapsedTime(time)}ms)") // 18992484
}