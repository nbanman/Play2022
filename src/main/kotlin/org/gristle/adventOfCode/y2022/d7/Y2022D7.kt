package org.gristle.adventOfCode.y2022.d7

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput

class Y2022D7(input: String) {

    class Directory(
        val name: String,
        val parent: Directory? = null,
        val directories: MutableMap<String, Directory> = mutableMapOf(),
        val files: MutableMap<String, Int> = mutableMapOf()
    ) {
        val size: Int by lazy {
            files.values.sum() + directories.values.sumOf { it.size }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Directory

            if (name != other.name) return false
            if (parent != other.parent) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + (parent?.hashCode() ?: 0)
            return result
        }
    }

    private val changeDir = Regex("""\$ cd [A-z]""")
    private val upDir = Regex("""\$ cd \.\.""")
    private val homeDirectory = Directory("/")
    var currentDirectory = homeDirectory
    private val allDirectories = mutableSetOf(homeDirectory)
    private fun String.lastWord() = split(" ").last()

    init {
        input.lines().forEach { line ->
            when {
                changeDir.containsMatchIn(line) -> {
                    currentDirectory = currentDirectory.directories[line.lastWord()]
                        ?: throw IllegalArgumentException("$line refers to directory that hasn't been created!")
                }

                upDir.containsMatchIn(line) -> {
                    currentDirectory = currentDirectory.parent ?: homeDirectory
                }

                line[0] == 'd' -> {
                    val dirName = line.lastWord()
                    val newDir = Directory(dirName, currentDirectory)
                    currentDirectory.directories[dirName] = newDir
                    allDirectories.add(newDir)
                }

                line[0].isDigit() -> {
                    val (fileSize, fileName) = line.split(" ")
                    currentDirectory.files[fileName] = fileSize.toInt()
                }
            }
        }
    }


    fun part1() = allDirectories.filter { it.size <= 100000 }.sumOf(Directory::size)

    fun part2() = allDirectories.filter {
        val spaceAvailable = 70000000L - homeDirectory.size
        val minDirSize = 30000000L - spaceAvailable
        it.size >= minDirSize // predicate
    }.minOf { it.size }
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