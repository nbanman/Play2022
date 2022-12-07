package org.gristle.adventOfCode.y2022.d7

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput

class Y2022D7(input: String) {

    class Directory(
        val name: String,
        val parent: Directory? = null,
        val directories: MutableMap<String, Directory> = mutableMapOf(),
        var totalFileSize: Int = 0
    ) {
        val size: Int by lazy {
            totalFileSize + directories.values.sumOf { it.size }
        }
    }

    private val changeDir = Regex("""\$ cd [A-z]""")
    private val upDir = Regex("""\$ cd \.\.""")
    private val homeDirectory = Directory("/")
    private val allDirectories = mutableListOf(homeDirectory)
    private fun String.lastWord() = split(" ").last()

    init {
        input.lines().fold(homeDirectory) { current, line ->
            when {
                changeDir.containsMatchIn(line) -> current.directories[line.lastWord()] ?: current
                upDir.containsMatchIn(line) -> current.parent ?: current
                line[0] == 'd' -> current.apply {
                    val dirName = line.lastWord()
                    val newDir = Directory(dirName, this)
                    directories[dirName] = newDir
                    allDirectories.add(newDir)
                }
                line[0].isDigit() -> current.apply { totalFileSize += line.takeWhile { it != ' ' }.toInt() }
                else -> current
            }
        }
    }

    fun part1() = allDirectories.filter { it.size <= 100000 }.sumOf(Directory::size)

    fun part2() = allDirectories
        .filter {
            val spaceAvailable = 70000000L - homeDirectory.size
            val minDirSize = 30000000L - spaceAvailable
            it.size >= minDirSize // predicate
        }.minOf(Directory::size)
}

fun main() {
    val input = listOf(
        getInput(7, 2022),
    )
    val timer = Stopwatch(start = true)
    val solver = Y2022D7(input[0])
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 1477771
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 3579501
    println("Total time: ${timer.elapsed()}ms")
}