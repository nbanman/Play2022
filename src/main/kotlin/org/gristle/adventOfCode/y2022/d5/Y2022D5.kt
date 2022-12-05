package org.gristle.adventOfCode.y2022.d5

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.blankSplit
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.groupValues
import java.util.*

private typealias Stacks = List<ArrayDeque<Char>>

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

    private val stacks: Stacks = List((crates.first().length + 1) / 4) { ArrayDeque<Char>() }
        .apply {
            crates.forEach { line ->
                for (index in 1..line.length step 4) {
                    val crateValue = line[index]
                    // note that for building the stacks up we use the add command, instead of push. That puts the 
                    // element at the bottom, not the top.
                    if (crateValue != ' ') get(index / 4).add(crateValue)
                }
            }
        }

    /**
     * Solve by cloning the stacks, following the instructions to rearrange the crates, then outputting the top
     * crate in each stack.
     *
     * Takes a 'rearrange' function to delegate the rearrangement to each part.
     */
    fun solve(rearrange: (stacks: Stacks, instruction: Instruction) -> Unit): String {
        // stacks are mutable, so clone the stacks so that they can be reused 
        val localStacks = stacks.map { it.clone() }
        instructions.forEach { instruction -> rearrange(localStacks, instruction) }
        return buildString { localStacks.forEach { stack -> append(stack.first) } }
    }

    /**
     * Building block for the 'rearrange' function, moving crates from one stack to another.
     */
    fun move(amount: Int, fromStack: ArrayDeque<Char>, toStack: ArrayDeque<Char>) {
        repeat(amount) { toStack.push(fromStack.pop()) }
    }
    
    fun part1() = solve { stacks, inst -> move(inst.amount, stacks[inst.fromStack], stacks[inst.toStack]) }

    // Same as part1 except instead of moving directly from one stack to another, we put them in a separate
    // stack to reverse the order that they are moved twice (thus canceling each other out and maintaining the 
    // original order).
    fun part2() = solve { stacks, inst ->
        val holdingBay = ArrayDeque<Char>()
        move(inst.amount, stacks[inst.fromStack], holdingBay) // move to holding stack
        move(inst.amount, holdingBay, stacks[inst.toStack]) // move from holding stack to new stack
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