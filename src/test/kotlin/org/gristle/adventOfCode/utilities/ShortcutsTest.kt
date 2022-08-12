package org.gristle.adventOfCode.utilities

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ShortcutsTest {
    @Test
    internal fun fmodTest() {
        assertEquals(1, 3 fmod 2)
        assertEquals(0, 3 fmod 3)
        assertEquals(2, -4 fmod 3)
    }


}