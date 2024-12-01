package org.gristle.adventOfCode.ec

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.Xyz
import org.gristle.adventOfCode.utilities.splitAt
import java.util.ArrayDeque

fun main() {
    val (input1, input2, input3) = ecInputs(24, 14)
    val timer = Stopwatch(true)
    println("1. ${part1(input1)}: ${timer.lap()}ms")
    println("2. ${part2(input2)}: ${timer.lap()}ms")
    println("3. ${part3(input3)}: ${timer.lap()}ms")
    println("Total: ${timer.stop()}ms")
}

private fun part1(input: String): Int = growBranch(input).maxOf { it.y }

private fun part2(input: String): Int = input
    .lineSequence()
    .flatMap { line -> growBranch(line) }
    .toSet()
    .size

private fun part3(input: String): Int {
    val branches: List<List<Xyz>> = input
        .lines()
        .map { line -> growBranch(line).toList() }
    val leaves = branches
        .map { branch -> branch.last() }
    val tree = branches.flatten().toSet()
    val treeHeight = tree.maxOf { it.y }
    val tapPoints = List(treeHeight) { IntArray(leaves.size) }
    val offsets = listOf(
        Xyz(1, 0, 0),
        Xyz(-1, 0, 0),
        Xyz(0, 1, 0),
        Xyz(0, -1, 0),
        Xyz(0, 0, 1),
        Xyz(0, 0, -1),
    )
    for ((leafIndex, leaf) in leaves.withIndex()) {
        var taps = 0
        val q = ArrayDeque<Pair<Xyz, Int>>()
        q.add(leaf to 0)
        val visited = mutableSetOf<Xyz>()
        while (q.isNotEmpty()) {
            val (pos, weight) = q.pop()

            // skip because already visited
            if (!visited.add(pos)) continue

            // trunk found
            if (pos.x == 0 && pos.z == 0) {
                tapPoints[pos.y - 1][leafIndex] = weight

                // end condition met
                if (++taps == treeHeight) break
            }

            // get neighbors
            offsets
                .map { pos + it }
                .filter { it !in visited && it in tree }
                .map { (x, y, z) -> Xyz(x, y, z) to weight + 1 }
                .forEach { state -> q.add(state) }
        }
    }
    return tapPoints.map { it.sum() }.filter { it != 0 }.min()
}

private fun growBranch(input: String) = input
    .splitToSequence(',')
    .flatMap { instruction ->
        val (dir, dist) = instruction.splitAt(1)
        val direction = dir[0]
        val distance = dist.toInt()
        generateSequence { direction }.take(distance)
    }.runningFold(Xyz(0, 0, 0)) { pos, direction ->
        pos + when (direction) {
            'U' -> Xyz(0, 1, 0)
            'D' -> Xyz(0, -1, 0)
            'L' -> Xyz(-1, 0, 0)
            'R' -> Xyz(1, 0, 0)
            'F' -> Xyz(0, 0, 1)
            'B' -> Xyz(0, 0,-1)
            else -> throw IllegalArgumentException("Unrecognized direction: $direction")
        }
    }.drop(1) // initial value for runningFold not part of branch
