package org.gristle.adventOfCode.utilities

enum class Nsew {
    NORTH {
        override fun left() = WEST
        override fun right() = EAST
        override fun forward(c: Coord, distance: Int) = Coord(c.x, c.y - distance)
    },

    SOUTH {
        override fun left() = EAST
        override fun right() = WEST
        override fun forward(c: Coord, distance: Int) = Coord(c.x, c.y + distance)
    },

    EAST {
        override fun left() = NORTH
        override fun right() = SOUTH
        override fun forward(c: Coord, distance: Int) = Coord(c.x + distance, c.y)
    },

    WEST {
        override fun left() = SOUTH
        override fun right() = NORTH
        override fun forward(c: Coord, distance: Int) = Coord(c.x - distance, c.y)
    };

    abstract fun left(): Nsew
    abstract fun right(): Nsew
    fun opposite() = left().left()
    abstract fun forward(c: Coord, distance: Int = 1): Coord
    fun forwardInclusive(c: Coord, distance: Int = 1): List<Coord> {
        return (1..distance).map {
            forward(c, it)
        }
    }
}