package org.gristle.adventOfCode.utilities

fun <E : Comparable<E>> Pair<E, E>.min() = if (first <= second) first else second

fun <E : Comparable<E>> Pair<E, E>.max() = if (first >= second) first else second

/**
 * Efficiently provides the minimum and maximum of a group of comparable objects
 */
fun <E : Comparable<E>> Iterable<E>.minMax(): Pair<E, E> {
    var min = first()
    var max = first()
    drop(1).forEach { if (it < min) min = it else if (it > max) max = it }
    return min to max
}

fun <E : Comparable<E>> minMax(vararg items: E): Pair<E, E> {
    var min = items.first()
    var max = items.first()
    items.drop(1).forEach { if (it < min) min = it else if (it > max) max = it }
    return min to max
}