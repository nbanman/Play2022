package org.gristle.adventOfCode.utilities

fun Sequence<Pair<Long, Long>>.crt(): Long {
    val n = fold(1L) { acc, (mod, _) -> acc * mod }
    val bigPhase = sumOf { (mod, remainder) ->
        val ni = (n / mod)
        remainder * ni * (ni.toBigInteger().modInverse(remainder.toBigInteger())).toLong()
    }
    return bigPhase % n
}
