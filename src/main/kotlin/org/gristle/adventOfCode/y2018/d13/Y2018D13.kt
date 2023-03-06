package org.gristle.adventOfCode.y2018.d13

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.toGrid

class Y2018D13(private val input: String) : Day {

    data class Car(
        val c: Coord,
        val direction: Nsew,
        val turnState: TurnState = TurnState.LEFT,
        val crashed: Boolean = false
    )

    enum class TurnState {
        LEFT {
            override fun advance() = STRAIGHT
        },

        STRAIGHT {
            override fun advance() = RIGHT
        },

        RIGHT {
            override fun advance() = LEFT
        };

        abstract fun advance(): TurnState
    }

    val solution: Pair<Coord, Coord> = let {
        val maze = "$input       ".toGrid()
        var firstCrash = Coord.ORIGIN
        var cars = maze
            .mapIndexedNotNull { index, c ->
                if (c in "<^>v") {
                    Car(
                        maze.coordOf(index),
                        when (c) {
                            '^' -> Nsew.NORTH
                            'v' -> Nsew.SOUTH
                            '>' -> Nsew.EAST
                            else -> Nsew.WEST
                        }
                    )
                } else null
            }.sortedBy { maze.indexOf(it.c) }
        while (cars.size > 1) {
            val newLocations = mutableListOf<Coord>()
            val crashLocations = mutableListOf<Coord>()
            val oldLocations = cars.map { it.c }.toMutableList()
            cars = cars
                .map { car ->
                    if (car.c in newLocations) {
                        Car(car.c, car.direction, car.turnState, true)
                    } else {
                        oldLocations.remove(car.c)
                        val spot = maze[car.c]
                        val newDirection = when (spot) {
                            '\\' -> {
                                when (car.direction) {
                                    Nsew.NORTH -> car.direction.left()
                                    Nsew.SOUTH -> car.direction.left()
                                    Nsew.EAST -> car.direction.right()
                                    Nsew.WEST -> car.direction.right()
                                }
                            }
                            '/' -> {
                                when (car.direction) {
                                    Nsew.NORTH -> car.direction.right()
                                    Nsew.SOUTH -> car.direction.right()
                                    Nsew.EAST -> car.direction.left()
                                    Nsew.WEST -> car.direction.left()                    }
                            }
                            '+' -> {
                                when (car.turnState) {
                                    TurnState.LEFT -> car.direction.left()
                                    TurnState.STRAIGHT -> car.direction
                                    TurnState.RIGHT -> car.direction.right()
                                }
                            }
                            else -> {
                                car.direction
                            }
                        }

                        val newState = if (spot == '+') car.turnState.advance() else car.turnState

                        val newLocation = newDirection.forward(car.c)

                        val gotCrashed = if (newLocation in newLocations || newLocation in oldLocations) {
                            if (firstCrash == Coord.ORIGIN) firstCrash = newLocation
                            crashLocations.add(newLocation)
                            true
                        } else false

                        newLocations.add(newLocation)

                        Car(newLocation, newDirection, newState, gotCrashed)
                    }
                }
            cars = cars
                .filter { it.c !in crashLocations && !it.crashed }
                .sortedBy { maze.indexOf(it.c) }
        }
        firstCrash to cars.first().c
    }

    override fun part1() = solution.first

    override fun part2() = solution.second
}

fun main() = Day.runDay(Y2018D13::class)
//    
//    Class creation: 80ms
//    Part 1: (86, 118) (0ms)
//    Part 2: (2, 81) (0ms)
//    Total time: 80ms