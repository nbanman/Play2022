package org.gristle.adventOfCode.cq

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getPairs
import org.gristle.adventOfCode.utilities.pow
import kotlin.math.pow

fun main() {
    val pairs = List(10) { it }.getPairs()
    val times = 1_000_000
    val sw = Stopwatch(true)
    while (true) {
        val fold = repeat(times) { pairs.map { (l, r) -> l.pow(r) } }
        println("fold: ${sw.lap()}")
        val cast = repeat(times) { pairs.map { (l, r) -> l.toDouble().pow(r) } }
        println("cast: ${sw.lap()}")
    }
}