package org.gristle.adventOfCode.y2018.d19

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.y2018.shared.Ops

fun List<Long>.store(pointer: Int, location: Int, result: Long): List<Long> {
    val newReg = mapIndexed { index, i ->
        if (index == location) result else i
    }
    return newReg.mapIndexed { index, l ->  if (index == pointer) l + 1 else l }
}

data class Command(val op: Ops, val p: Int, val a: Int, val b: Int, val c: Int) {
    fun execute(reg: List<Long>) = op.fn(reg, p, a, b, c)
}

class Y2018D19(input: String) : Day {
    private val data = input.lines()

    override fun part1(): Long {
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

    override fun part2(): Int {
//     Loop is such that R3 starts as 1, R5 goes up by 1. R2 is R3 * R5. When R2 equals 105k, R0+= R3 and R3++, R5 resets
//     If R2 goes past 105K w/o equaling it (not divisible), then R3++ and R5 resets w/o RO going up. Thus, RO adds all
//     the numbers that divide evenly into 105k.
        val targetNum = 10_551_370
        return (1..targetNum).fold(0) { acc, i ->
            if (targetNum % i == 0) acc + i else acc
        }
    }
}

fun main() = Day.runDay(Y2018D19::class)

//    Class creation: 19ms
//    Part 1: 1764 (1259ms)
//    Part 2: 18992484 (83ms)
//    Total time: 1362ms
