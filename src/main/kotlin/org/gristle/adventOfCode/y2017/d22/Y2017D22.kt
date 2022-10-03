package org.gristle.adventOfCode.y2017.d22

import org.gristle.adventOfCode.utilities.*

// not refactored!
class Y2017D22(private val input: String) {
    class Virus(private var location: Coord, private var orientation: String, private val space: MutableGrid<Boolean>) {
        var infections = 0
        fun burst() {
            if (space[location]) {
                space[location] = false
                when (orientation) {
                    "up" -> {
                        orientation = "right"
                        location += Coord(1, 0)
                    }
                    "right" -> {
                        orientation = "down"
                        location += Coord(0, 1)
                    }
                    "down" -> {
                        orientation = "left"
                        location += Coord(-1, 0)
                    }
                    "left" -> {
                        orientation = "up"
                        location += Coord(0, -1)
                    }
                }
            } else {
                space[location] = true
                infections++
                when (orientation) {
                    "up" -> {
                        orientation = "left"
                        location += Coord(-1, 0)
                    }
                    "right" -> {
                        orientation = "up"
                        location += Coord(0, -1)
                    }
                    "down" -> {
                        orientation = "right"
                        location += Coord(1, 0)
                    }
                    "left" -> {
                        orientation = "down"
                        location += Coord(0, 1)
                    }
                }
            }
        }
    }

    class Virus2(private var location: Coord, private var orientation: String, private val space: MutableGrid<NState>) {
        var infections = 0
        fun burst() {
            val state = space[location]
            space[location] = space[location].advance()
            if (space[location] == NState.INFECTED) infections++
            when (state) {
                NState.CLEAN -> {
                    when (orientation) {
                        "up" -> {
                            orientation = "left"
                            location += Coord(-1, 0)
                        }
                        "right" -> {
                            orientation = "up"
                            location += Coord(0, -1)
                        }
                        "down" -> {
                            orientation = "right"
                            location += Coord(1, 0)
                        }
                        "left" -> {
                            orientation = "down"
                            location += Coord(0, 1)
                        }
                    }
                }
                NState.WEAKENED -> {
                    when (orientation) {
                        "up" -> {
                            location += Coord(0, -1)
                        }
                        "right" -> {
                            location += Coord(1, 0)
                        }
                        "down" -> {
                            location += Coord(0, 1)
                        }
                        "left" -> {
                            location += Coord(-1, 0)
                        }
                    }
                }
                NState.INFECTED -> {
                    when (orientation) {
                        "up" -> {
                            orientation = "right"
                            location += Coord(1, 0)
                        }
                        "right" -> {
                            orientation = "down"
                            location += Coord(0, 1)
                        }
                        "down" -> {
                            orientation = "left"
                            location += Coord(-1, 0)
                        }
                        "left" -> {
                            orientation = "up"
                            location += Coord(0, -1)
                        }
                    }
                }
                NState.FLAGGED -> {
                    when (orientation) {
                        "up" -> {
                            orientation = "down"
                            location += Coord(0, 1)
                        }
                        "right" -> {
                            orientation = "left"
                            location += Coord(-1, 0)
                        }
                        "down" -> {
                            orientation = "up"
                            location += Coord(0, -1)
                        }
                        "left" -> {
                            orientation = "right"
                            location += Coord(1, 0)
                        }
                    }
                }
            }
        }
    }

    enum class NState {
        CLEAN {
            override fun advance() = WEAKENED
        },
        WEAKENED {
            override fun advance() = INFECTED
        },
        INFECTED {
            override fun advance() = FLAGGED
        },
        FLAGGED {
            override fun advance() = CLEAN
        };

        abstract fun advance(): NState
    }
    
    fun part1(): Int {
        val core = input.toGrid { it == '#' }
        val blank = List(500_500) { false }.toGrid(1_001)
        val coreFiller = List(((1_001 - core.width) / 2) * core.height) { false }.toGrid(((1_001 - core.width) / 2))
        val coreLines = coreFiller.addRight(core).addRight(coreFiller)
        val space = blank
            .addDown(coreLines)
            .addDown(blank)
            .toMutableGrid(1_001)
        val virus = Virus(Coord(space.width / 2, space.height / 2), "up", space)
        for (x in 1..10_000) { virus.burst() }
        return virus.infections

    }
    
    fun part2(): Int {
        val core = input.toGrid { if (it == '#') NState.INFECTED else NState.CLEAN }
        val blank = List(500_500) { NState.CLEAN }.toGrid(1_001)
        val coreFiller = List(((1_001 - core.width) / 2) * core.height) { NState.CLEAN }
            .toGrid(((1_001 - core.width) / 2))
        val coreLines = coreFiller.addRight(core).addRight(coreFiller)
        val space = blank
            .addDown(coreLines)
            .addDown(blank)
            .toMutableGrid(1_001)
        val virus = Virus2(Coord(space.width / 2, space.height / 2), "up", space)
        for (x in 1..10_000_000) { virus.burst() }
        return virus.infections
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2017D22(readRawInput("y2017/d22"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 5348
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 2512225
}