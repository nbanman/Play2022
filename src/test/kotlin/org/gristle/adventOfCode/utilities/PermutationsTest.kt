package org.gristle.adventOfCode.utilities

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PermutationsTest {
    @Test
    internal fun findsValidPermutation() {
        val permutations = "ABCDE".toList().getPermutations()
        assertEquals(120, permutations.size)
    }

    @Test
    internal fun findsValidPermutationWithSeed() {
        val permutations = "ABCDEF".toList().getPermutations(listOf('A', 'B'))
        assertEquals(24, permutations.size)
    }
}