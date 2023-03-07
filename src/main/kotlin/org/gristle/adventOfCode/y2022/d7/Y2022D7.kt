package org.gristle.adventOfCode.y2022.d7

import org.gristle.adventOfCode.Day

class Y2022D7(input: String) : Day {

    // Directory node tracks child Directories in a map, and tracks the total file size of any files assigned to it 
    // or any child directory.
    private class Directory(val name: String) {
        val directories: MutableMap<String, Directory> = mutableMapOf()
        var fileSize: Int = 0 // not just size of files in this directory, but files in child directories as well

        // Provides a list of all directories in and under this directory.
        fun inclusiveDirectories(): List<Directory> =
            directories.values.toList() + directories.values.flatMap(Directory::inclusiveDirectories)
    }

    // Parsing input to create file structure
    private fun createFileStructure(input: String): Directory {

        // tracks the full path to current dir, starting with root.
        val path: MutableList<Directory> = mutableListOf(Directory("/"))

        input.lines().forEach { line ->
            when {
                line.startsWith("\$ cd /") -> repeat(path.size - 1) { path.removeLast() } // $ cd / 
                line.startsWith("\$ cd ..") -> if (path.size > 1) path.removeLast() // $ cd ..
                line.startsWith("\$ cd") -> { // cd [directory]
                    val dir = line.takeLastWhile { it != ' ' } // grabs last word in the String
                    path.add(path.last().directories.getOrPut(dir) { Directory(dir) })
                }

                line[0].isDigit() -> { // increase fileSize of all Directories in the path
                    val fileSize = line.takeWhile { it != ' ' }.toInt()
                    path.forEach { dir -> dir.fileSize += fileSize }
                }
            }
        }
        return path.first()
    }

    private val root = createFileStructure(input)
    private val allDirectories = root.inclusiveDirectories()

    override fun part1() = allDirectories.filter { it.fileSize <= 100000 }.sumOf(Directory::fileSize)

    override fun part2() = allDirectories
        .filter {
            val spaceAvailable = 70000000L - root.fileSize
            val minDirSize = 30000000L - spaceAvailable
            it.fileSize >= minDirSize // predicate
        }.minOf(Directory::fileSize)
}

fun main() = Day.runDay(Y2022D7::class) // 1477771, 3579501