package org.gristle.adventOfCode.utilities

enum class Nsew {
    NORTH {
        override fun multiLeft() = WEST
        override fun multiRight() = EAST
        override fun forward(c: Coord, distance: Int) = Coord(c.x, c.y - distance)
    },

    SOUTH {
        override fun multiLeft() = EAST
        override fun multiRight() = WEST
        override fun forward(c: Coord, distance: Int) = Coord(c.x, c.y + distance)
    },

    EAST {
        override fun multiLeft() = NORTH
        override fun multiRight() = SOUTH
        override fun forward(c: Coord, distance: Int) = Coord(c.x + distance, c.y)
    },

    WEST {
        override fun multiLeft() = SOUTH
        override fun multiRight() = NORTH
        override fun forward(c: Coord, distance: Int) = Coord(c.x - distance, c.y)
    };

    abstract fun multiLeft(): Nsew
    abstract fun multiRight(): Nsew
    fun opposite() = multiLeft().multiLeft()
    abstract fun forward(c: Coord, distance: Int = 1): Coord
    fun forwardInclusive(c: Coord, distance: Int = 1): List<Coord> {
        return (1..distance).map {
            forward(c, it)
        }
    }

    fun multiLeft(times: Int): Nsew = (1..times).fold(this) { acc, _ -> acc.multiLeft() }
    fun multiRight(times: Int): Nsew = (1..times).fold(this) { acc, _ -> acc.multiRight() }
}