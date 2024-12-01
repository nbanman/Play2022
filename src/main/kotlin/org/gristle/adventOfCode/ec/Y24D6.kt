package org.gristle.adventOfCode.ec

import org.gristle.adventOfCode.utilities.Stopwatch
import java.lang.IllegalArgumentException

private typealias Branches = Map<String, List<String>>

fun main() {
    val (input1, input2, input3) = ecInputs(24, 6)
    val timer = Stopwatch(true)
    println("1. ${solve(input1, false)}: ${timer.lap()}ms")
    println("2. ${solve(input2, true)}: ${timer.lap()}ms")
    println("3. ${solve(input3, true)}: ${timer.lap()}ms")
    println("Total: ${timer.stop()}ms")
}

private fun solve(input: String, truncate: Boolean): String {
    val branches = getBranches(input)
    val paths = getPaths(branches, truncate)
    return getStrongest(paths)
}

private fun getBranches(input: String): Branches = input.lines()
    .filter { line -> !line.startsWith("ANT") && !line.startsWith("BUG") }
    .map { line ->
        val tokens = line.split(':', ',')
        tokens[0] to tokens.drop(1)
    }.toMap()

private fun getPaths(branches: Branches, truncate: Boolean) = buildList<String> {
    val q = mutableListOf(listOf("RR"))
    while (q.isNotEmpty()) {
        val path = q.removeLast()
        val current = path.last()
        if (current == "@") {
            val pathName = buildString {
                if (truncate) {
                    for (s in path) append(s.first())
                } else {
                    append(path.joinToString(""))
                }
            }
            add(pathName)
        } else {
            branches[current]?.forEach { child ->
                q.add(path + child)
            }
        }
    }
}

private fun getStrongest(paths: List<String>): String = paths
    .groupBy { it.length }
    .values
    .find { it.size == 1 }
    ?.first()
    ?: throw IllegalArgumentException("All values have a matching length value.")