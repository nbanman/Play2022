package org.gristle.adventOfCode.y2015.d12

import org.gristle.adventOfCode.Day

// refactor candidate: lots of vars, while loops, may as well be js
class Y2015D12(private val input: String) : Day {

    data class Block(val value: Int, val length: Int)

    private val pattern = Regex("""(-?\d+)""")

    private fun getBlockValue(input: String): Block {
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

    override fun part1() = pattern
        .findAll(input)
        .map { it.value.toInt() }
        .sum()

    override fun part2() = getBlockValue(input).value
}

fun main() = Day.runDay(Y2015D12::class)

//    Class creation: 20ms
//    Part 1: 111754 (8ms)
//    Part 2: 65402 (12ms)
//    Total time: 41ms