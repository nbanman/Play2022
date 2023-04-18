package org.gristle.adventOfCode.cq

private typealias Pos = Pair<Int, Int>

fun main() {
    val (length, txt) = input[0]
    val lines = txt.lines()
    val fruit = lines[1]
        .split(',', ' ')
        .map { it.toInt() }
        .chunked(2) { (x, y) -> x to y }
        .iterator()
    val moves = lines[3]

    val snake = java.util.ArrayDeque<Pos>().apply { add(0 to 0) }
    var score = 0
    var nextFruit = fruit.next()
    for (move in moves) {
        val (x, y) = snake.last
        val newHead = when (move) {
            'U' -> x to y - 1
            'R' -> x + 1 to y
            'L' -> x - 1 to y
            'D' -> x to y + 1
            else -> throw IllegalArgumentException("Invalid move.")
        }
        if (newHead.first !in 0 until length || newHead.second !in 0 until length) break

        if (newHead == nextFruit) {
            nextFruit = if (fruit.hasNext()) fruit.next() else Pos(-1, -1)
            score += 100
        } else {
            snake.removeFirst()
        }

        if (newHead in snake) break
        snake.addLast(newHead)
        score++
    }
    println(score)
}

private val input = listOf(
    20 to """fruit
5,5 8,17 5,2 17,14 2,4 17,6 17,17 1,1 2,3 4,9 13,2 12,15 18,15 12,1 17,5 2,14 7,3 17,6 7,13 6,5 5,17 17,12 16,7 15,15 14,14 10,8 15,5 12,12 9,18 7,16 1,3 16,13 12,11 13,6 11,1 5,4 15,8 6,3 5,14 5,3 5,1 17,12 10,14 13,14 18,14 6,14 7,1 15,16 13,4 18,3 9,1 3,13
moves
RRRRRRRRRRDDDDDDDDDDUUUUULLLLLLDDDDDDDDDDDDRRRRRRUUUUUUUUUUULLLLLLUUUURRDDRDRDDRDRDRDRDDDDRRRRRRUULLLLLLDLDLDLUUUUUUUULLLLLLLLUUURRRRDDDRRRRRRRRRRRRUUULLDDDRRDDDDDDDDDDLLLLLLLLLLLUUUUUUUUUUUUUUUULLLLLDDDRURRDDDDDDLLDRRRRRRUUUUUUURURRRRRRDDDDDDDDDDDDDLLLLLDRRRRRRRRUUUUUULLLLLLLUUUUUUUUURRRDDDDDDLLLDDRRRRUUUUURRDDDDDDLLLLLLLLLLLLLLLDDDDRRRRUUURUUUUUUUUURRRRRDDDDRRRRRDDDDDDLLLLLLDLLLLLUUUUUUUUURRDDDDDDDDDDDDDLLLLUUUUURRRUUURRDDDDLLDRRRRRRRRRRUUUUUUULLLDDDRDDDLLLDDRRRRRRUUUUUUUUUUUUULLLLDDDDDDDDDDDDDDLLLLUUUUUUUUUUURRRRRRRDDDDDDDLLLLLLDDDDDLDLLLUUUUUULLLLLLLUUUUUUUUURRRRRRRRRDDDDDDDDDDRRRRRRRRUULLLLLLUUUUURRRUUUUULLLLDDLDLLLLLDDDDRRRRRRRRRRRRRUUUUUUUULLLLLLLLLLLDDDDLLLDDDDDDDDDDRRRRRRRRRRRRRRRUUUUUUUUUULLLLLLLLLULLLLLLLUURRRRRRRRRRRRRRRRRDDDDDDDDDDDLLLLLLLLLDDDDLLLLLLLLDRRRRRRRRRUUURRRRRRRRDDDDLLLLLLLLLLLLLLLLLLUUUURRRRRRRULLLLLLLUUUUUUUUUUUURRRRRRRRDDDDDRDDDDDDDDDDDRRRRRDRRRRUULLLLLLLURRRRRRRULLLLLLLURRRRRRRULLLLLLLURRRRRRRULLLLLLLURRRRRRRULLLLLLLURRRRRRRULLLLLLLURRRRRRRULLLLLLLURRRRRRRUULLLLLLLLLLLLLLLLDDDDDDDD
""",
    8 to """fruit
3,3 2,5 7,7 6,0
moves
DDDRRRDDLLLDRRRRRRRDD"""
)