package org.gristle.adventOfCode.utilities

import org.gristle.adventOfCode.y2016.d13.Y2016D13
import org.gristle.adventOfCode.y2016.d24.Y2016D24
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BfsTest {
    @Test
    internal fun doesntBreakAocs() {
        assertEquals(470, Y2016D24.part1())
        assertEquals(92, Y2016D13.part1())
    }
}