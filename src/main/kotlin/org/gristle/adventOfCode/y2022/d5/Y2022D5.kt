package org.gristle.adventOfCode.y2022.d5

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.blankSplit
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.groupValues
import java.util.*

class Y2022D5(input: String) {

    data class Instruction(val amount: Int, val fromStack: Int, val toStack: Int)

    private val parsed = input.blankSplit()

    val instructions = parsed[1]
        .groupValues("""move (\d+) from (\d+) to (\d+)""", String::toInt)
        .map { gv -> Instruction(gv[0], gv[1] - 1, gv[2] - 1) }


    val crates = parsed[0]
        .lines()
        .dropLast(1)
        .reversed()
        .map { line ->
            line
                .chunked(4)
                .map { chunk -> chunk.firstOrNull() { it.isUpperCase() } }
        }

    private val stacks: List<Deque<Char>> = List(crates.first().size) { ArrayDeque<Char>() }

    private val stacks2: List<Deque<Char>> = List(crates.first().size) { ArrayDeque<Char>() }

    init {
        crates.forEach { line ->
            line.forEachIndexed { index, c ->
                if (c != null) {
                    stacks[index].push(c)
                    stacks2[index].push(c)
                }
            }
        }
    }

    fun part1(): String {
        instructions.forEach { (amount, fromStack, toStack) ->
            repeat(amount) {
                stacks[toStack].push(stacks[fromStack].pop())
            }
        }
        return buildString {
            stacks.forEach { stack -> append(stack.first) }
        }
    }

    fun part2(): String {
        println("start: $stacks2")
        instructions.forEach { (amount, fromStack, toStack) ->
            val holdingBay: Deque<Char> = ArrayDeque<Char>()
            repeat(amount) {
                holdingBay.push(stacks2[fromStack].pop())
            }
            println("holding bay: $holdingBay")
            repeat(amount) {
                stacks2[toStack].push(holdingBay.pop())
            }
        }
        return buildString {
            stacks2.forEach { stack -> append(stack.first) }
        }
    }
}

fun main() {
    val input = listOf(
        getInput(5, 2022),
        """    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2""",
    )
    val timer = Stopwatch(start = true)
    val solver = Y2022D5(input[0])
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 
    println("Total time: ${timer.elapsed()}ms")
}