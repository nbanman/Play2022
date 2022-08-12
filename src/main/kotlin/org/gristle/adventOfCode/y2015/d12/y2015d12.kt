package org.gristle.adventOfCode.y2015.d12

import org.gristle.adventOfCode.utilities.*

object Y2015D12 {
    private val input = readRawInput("y2015/d12")

    data class Block(val value: Int, val length: Int)

    private val pattern = Regex("""(-?\d+)""")

    fun getBlockValue(input: String): Block {
        var numValue = 0
        var index = 0
        var isRed = false
        while (index < input.length) {
            val blockIndex = input.substring(index).indexOfFirst { "[]{}".contains(it) } + index
            val snippet = if (blockIndex == -1) {
                input.substring(index)
            } else {
                input.substring(index, blockIndex)
            }
            if (snippet.contains("\"red\"")) {
                isRed = true
            }
            numValue += pattern
                .findAll(snippet)
                .map { it.value.toInt() }
                .sum()
            if (blockIndex >= 0) {
                if (input[blockIndex] == ']') return Block(numValue, blockIndex + 2)
                if (input[blockIndex] == '}') {
                    return if (isRed) Block(0, blockIndex + 2) else Block(numValue, blockIndex + 2)
                }
                val newBlock = getBlockValue(input.substring(blockIndex + 1))
                numValue += newBlock.value
                index = blockIndex + newBlock.length
            }

        }
        return if (isRed) Block(0, input.lastIndex) else Block(numValue, input.lastIndex)
    }

    fun part1() = pattern
        .findAll(input)
        .map { it.value.toInt() }
        .sum()

    fun part2() = getBlockValue(input).value
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D12.part1()} (${elapsedTime(time)}ms)") // 111754
    time = System.nanoTime()
    println("Part 2: ${Y2015D12.part2()} (${elapsedTime(time)}ms)") // 65402
}