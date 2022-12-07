package org.gristle.adventOfCode.y2022.d7

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput

class Y2022D7(input: String) {

    // Directory node tracks child Directories in a map, and tracks the total file size of any files assigned to it 
    // or any child directory.
    class Directory(
        val name: String,
        val directories: MutableMap<String, Directory> = mutableMapOf(),
        var fileSize: Int = 0 // not just size of files in this directory, but files in child directories as well
    ) {
        // Provides a list of all directories in and under this directory.
        fun inclusiveDirectories(): List<Directory> =
            directories.values.toList() + directories.values.flatMap(Directory::inclusiveDirectories)
    }

    private val root = Directory("/")
    private val path = mutableListOf(root) // tracks the full path to current dir, starting with root.

    // Parsing input to create file structure
    init {
        fun String.lastWord() = takeLastWhile { it != ' ' } // utility function grabs last word in a String
        input.lines().forEach { line ->
            when {
                line.startsWith("\$ cd /") -> repeat(path.size - 1) { path.removeLast() } // $ cd / 
                line.startsWith("\$ cd ..") -> if (path.size > 1) path.removeLast() // $ cd ..
                line.startsWith("\$ cd") -> path.last().directories[line.lastWord()]?.let { path.add(it) } // $ cd
                line[0].isDigit() -> { // increase fileSize of all Directories in the path
                    val fileSize = line.takeWhile { it != ' ' }.toInt()
                    path.forEach { dir -> dir.fileSize += fileSize }
                }
                line.startsWith("dir") -> { // add a new directory
                    val newDir = Directory(line.lastWord())
                    path.last().directories[newDir.name] = newDir
                }
            }
        }
    }

    private val allDirectories = root.inclusiveDirectories()

    fun part1() = allDirectories.filter { it.fileSize <= 100000 }.sumOf(Directory::fileSize)

    fun part2() = allDirectories
        .filter {
            val spaceAvailable = 70000000L - root.fileSize
            val minDirSize = 30000000L - spaceAvailable
            it.fileSize >= minDirSize // predicate
        }.minOf(Directory::fileSize)
}

fun main() {
    val input = getInput(7, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D7(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 1477771
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 3579501
    println("Total time: ${timer.elapsed()}ms")
}