package org.gristle.adventOfCode.utilities

import org.gristle.adventOfCode.y2016.d13.Y2016D13
import org.gristle.adventOfCode.y2016.d24.Y2016D24
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DfsTest {
    @Test
    internal fun doesntBreakAocs() {
        assertEquals(470, Y2016D24(readRawInput("y2016/d24")).part1())
        assertEquals(92, Y2016D13(readRawInput("y2016/d13")).part1())
    }
}