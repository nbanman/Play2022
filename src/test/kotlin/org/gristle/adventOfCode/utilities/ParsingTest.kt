package org.gristle.adventOfCode.utilities

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ParsingTest {
    @Test
    internal fun testLines() {
        val lines = "1234\n5678".lines()
        assertEquals("5678", lines.last())
    }

    @Test
    internal fun testGroupValues() {
        val input = "3x11x24\n13x5x19\n1x9x27\n24x8x21"
        val pattern = """(\d+)x(\d+)x(\d+)"""
        val expected = listOf(
            listOf("3", "11", "24"),
            listOf("13", "5", "19"),
            listOf("1", "9", "27"),
            listOf("24", "8", "21"),
        )
        assertEquals(expected, input.groupValues(pattern))
    }

    @Test
    internal fun testGroupValuesWithMap() {
        val input = "3x11x24\n13x5x19\n1x9x27\n24x8x21"
        val pattern = """(\d+)x(\d+)x(\d+)"""
        val expected = listOf(
            listOf(3, 11, 24),
            listOf(13, 5, 19),
            listOf(1, 9, 27),
            listOf(24, 8, 21),
        )
        assertEquals(expected, input.groupValues(pattern) { it.toInt() })
    }

}