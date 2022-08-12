package org.gristle.adventOfCode.utilities

import org.gristle.adventOfCode.y2018.d22.Y2018D22
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AStarTest {
    @Test
    internal fun aStarWorksMultidimensinally() {
        assertEquals(969, Y2018D22.part2())
    }
}