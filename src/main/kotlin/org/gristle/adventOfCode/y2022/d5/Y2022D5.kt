package org.gristle.adventOfCode.y2022.d5

import org.gristle.adventOfCode.utilities.*

private typealias Instruction = Triple<Int, Int, Int>

class Y2022D5(input: String) {
    // Split the input into two strings, the first describing the crate arrangement, and the second giving
    // rearrangement instructions.
    private val splitInputLines = input.lines().splitOnBlank()

    // Build crate stacks
    private val crateStacks = splitInputLines[0]
        .dropLast(1) // gets rid of line labeling the stack numbers
        .transpose() // flip the strings vertically
        .filter { it.last().isLetter() } // get rid of strings that don't have crate information
        .map { ArrayDeque(it.trimStart().toList()) } // create and load up stacks

    // Get instructions
    val instructions = splitInputLines[1].map {
        it.getIntList().let { intList -> Instruction(intList[0], intList[1] - 1, intList[2] - 1) }
    }

    /**
     * Solve by cloning the stacks, following the instructions to rearrange the crates, then outputting the top
     * crate in each stack.
     *
     * Takes a 'rearrange' function to delegate the rearrangement to each part.
     */
    fun solve(rearrange: (amount: Int, fromStack: ArrayDeque<Char>, toStack: ArrayDeque<Char>) -> Unit): String {
        // stacks are mutable, so clone the stacks so that they can be reused 
        val stacks = crateStacks.map { ArrayDeque(it) }
        instructions.forEach { (amount, fromIndex, toIndex) -> rearrange(amount, stacks[fromIndex], stacks[toIndex]) }
        return buildString { stacks.forEach { stack -> append(stack.first()) } }
    }

    /**
     * Building block for the 'rearrange' function, moving crates from one stack to another.
     */
    fun move(amount: Int, fromStack: ArrayDeque<Char>, toStack: ArrayDeque<Char>) {
        repeat(amount) { toStack.addFirst(fromStack.removeFirst()) }
    }

    fun part1() = solve { amount, fromStack, toStack ->
        move(amount, fromStack, toStack)
    }

    // Same as part1 except instead of moving directly from one stack to another, we put them in a separate
    // stack to reverse the order that they are moved twice (thus canceling each other out and maintaining the 
    // original order).
    fun part2() = solve { amount, fromStack, toStack ->
        val holdingBay = ArrayDeque<Char>()
        move(amount, fromStack, holdingBay) // move to holding stack
        move(amount, holdingBay, toStack) // move from holding stack to new stack
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