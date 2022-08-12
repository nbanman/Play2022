package org.gristle.adventOfCode.utilities

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class IoTest {
    @Test
    internal fun testReadRawInput() {
        val fileString = readRawInput("test", "testInput")
        assertEquals( "3x11", fileString.take(4))
    }
    @Test
    internal fun testReadInput() {
        val lines = readInput("test", "testInput")
        assertEquals( "3x11x24", lines.first())
    }
}