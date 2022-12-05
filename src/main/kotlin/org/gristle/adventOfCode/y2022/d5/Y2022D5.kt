package org.gristle.adventOfCode.y2022.d5

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.blankSplit
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.groupValues
import java.util.*

class Y2022D5(input: String) {

    private val parsed = input.blankSplit()

    // Get instructions
    data class Instruction(val amount: Int, val fromStack: Int, val toStack: Int)

    val instructions = parsed[1]
        .groupValues("""move (\d+) from (\d+) to (\d+)""", String::toInt)
        .map { gv -> Instruction(gv[0], gv[1] - 1, gv[2] - 1) }

    // Build crate stacks
    private val crates = parsed[0]
        .lines()
        .dropLast(1)

    private val stacks = List((crates.first().length + 1) / 4) { ArrayDeque<Char>() }
        .apply {
            // note that for building the stacks up we use the add command, instead of push. That puts the element at
            // the bottom, not the top.
            crates.forEach { line ->
                for (index in 1..line.length step 4) {
                    val crateValue = line[index]
                    if (crateValue != ' ') get(index / 4).add(crateValue)
                }
            }
        }

    fun part1(): String {
        // stacks are mutable, so clone the stacks so that they can be reused from starting position in part 2
        val stacks1 = List(stacks.size) {
            stacks[it].clone()
        }
        instructions.forEach { (amount, fromStack, toStack) -> // for each instruction
            repeat(amount) { // do the following n number of times
                stacks1[toStack].push(stacks1[fromStack].pop()) // make the move from one stack to another
            }
        }
        return buildString {
            stacks1.forEach { stack -> append(stack.first) }
        }
    }

    fun part2(): String {
        // Same as part1 except instead of moving directly from one stack to another, we put them in a separate
        // stack to reverse the order that they are moved twice (thus canceling each other out and maintaining the 
        // original order).
        instructions.forEach { (amount, fromStack, toStack) ->
            val holdingBay: Deque<Char> = ArrayDeque()
            repeat(amount) {// move to holding stack
                holdingBay.push(stacks[fromStack].pop())
            }
            repeat(amount) {// move from holding stack to new stack
                stacks[toStack].push(holdingBay.pop())
            }
        }
        return buildString {
            stacks.forEach { stack -> append(stack.first) }
        }
    }
}

fun main() {
    val input = getInput(5, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D5(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // ZSQVCCJLL
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // QZFJRWHGS
    println("Total time: ${timer.elapsed()}ms")
}