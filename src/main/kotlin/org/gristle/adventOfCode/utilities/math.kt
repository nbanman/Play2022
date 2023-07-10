package org.gristle.adventOfCode.utilities

import kotlin.math.sqrt

fun Sequence<Pair<Long, Long>>.crt(): Long {
    val n = fold(1L) { acc, (mod, _) -> acc * mod }
    val bigPhase = sumOf { (mod, remainder) ->
        val ni = (n / mod)
        remainder * ni * (ni.toBigInteger().modInverse(remainder.toBigInteger())).toLong()
    }
    return bigPhase % n
}

fun Int.isPrime(): Boolean = when {
    this <= 1 -> false
    this <= 3 -> true
    this.isEven() || this % 3 == 0 -> false
    else -> {
        val limit = sqrt(this.toFloat()).toInt()
        generateSequence(5) { it + 6 }
            .takeWhile { it <= limit }
            .none { this % it == 0 || this % (it + 2) == 0 }
    }
}

fun Int.factorial(): Long = (1..this).fold(1L, Long::times) 