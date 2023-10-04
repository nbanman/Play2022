package org.gristle.adventOfCode.utilities

enum class Nsew {
    NORTH {
        override fun left() = WEST
        override fun right() = EAST
        override fun flip() = SOUTH

        override fun forward(c: Coord, distance: Int) = Coord(c.x, c.y - distance)
    },

    SOUTH {
        override fun left() = EAST
        override fun right() = WEST
        override fun flip() = NORTH
        override fun forward(c: Coord, distance: Int) = Coord(c.x, c.y + distance)
    },

    EAST {
        override fun left() = NORTH
        override fun right() = SOUTH
        override fun flip() = WEST
        override fun forward(c: Coord, distance: Int) = Coord(c.x + distance, c.y)
    },

    WEST {
        override fun left() = SOUTH
        override fun right() = NORTH
        override fun flip() = EAST
        override fun forward(c: Coord, distance: Int) = Coord(c.x - distance, c.y)
    };

    abstract fun left(): Nsew
    abstract fun right(): Nsew
    abstract fun flip(): Nsew
    fun opposite() = left().left()
    abstract fun forward(c: Coord, distance: Int = 1): Coord
    fun forwardInclusive(c: Coord, distance: Int = 1): List<Coord> {
        return (1..distance).map {
            forward(c, it)
        }
    }

    fun multiLeft(times: Int): Nsew = (1..times).fold(this) { acc, _ -> acc.left() }
    fun multiRight(times: Int): Nsew = (1..times).fold(this) { acc, _ -> acc.right() }
}

fun Char.toNsew(): Nsew = when (this) {
    'R', 'E' -> Nsew.EAST
    'U', 'N' -> Nsew.NORTH
    'L', 'W' -> Nsew.WEST
    'D', 'S' -> Nsew.SOUTH
    else -> throw IllegalArgumentException("Invalid input")
}