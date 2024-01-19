package org.gristle.adventOfCode.utilities

class StringGrid(val string: String) {
    val width = string.indexOf('\n') + 1
    val height = (string.length + if (string.last() == '\n') 0 else 1) / width

    fun isValid(index: Int): Boolean = index in string.indices && (index + 1) % width != 0
    
    operator fun get(index: Int): Char = if (isValid(index)) string[index] else 
        throw IndexOutOfBoundsException("Index $index out of bounds.")
    
    fun getOrNull(index: Int): Char? = if (isValid(index)) string[index] else null
    
    fun move(index: Int, direction: Nsew, distance: Int = 1): Int = moveOrNull(index, direction, distance)
        ?: throw IndexOutOfBoundsException("Moved index out of bounds.")
    
    fun moveOrNull(index: Int, direction: Nsew, distance: Int = 1): Int? {
        val movement = when (direction) {
            Nsew.NORTH -> -(width * distance)
            Nsew.SOUTH -> width * distance
            Nsew.EAST -> distance
            Nsew.WEST -> -distance
        }
        return (index + movement).let { if (isValid(it)) it else null }
    }
        
    fun getNeighbors(index: Int) = Nsew.entries.mapNotNull { moveOrNull(index, it) }
}