package org.gristle.adventOfCode.utilities

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GridTest {
    val fileString = readRawInput("test", "gridTest")
    val grid = fileString.toMutableGrid()
    
    @Test
    internal fun getNeighborsTest() {
        val coord = Coord(22, 11)
        val runaround = grid.getNeighborIndices(coord).map { grid[it] }
        val same = grid.getNeighbors(coord)
        
        assertEquals(runaround, same)
    }
}