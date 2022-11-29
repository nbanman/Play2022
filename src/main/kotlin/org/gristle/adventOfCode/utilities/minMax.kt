package org.gristle.adventOfCode.utilities

/**
 * Returns the minimum of a pair of the same comparable object
 */
fun <E : Comparable<E>> Pair<E, E>.min() = if (first <= second) first else second

/**
 * Returns the maximum of a pair of the same comparable object
 */
fun <E : Comparable<E>> Pair<E, E>.max() = if (first >= second) first else second

/**
 * Efficiently provides the minimum and maximum of a group of comparable objects
 */
fun <E : Comparable<E>> Iterable<E>.minMax(): Pair<E, E> {
    var min = first()
    var max = first()
    forEach { if (it < min) min = it else if (it > max) max = it }
    return min to max
}

/**
 * Efficiently provides the minimum and maximum of a group of comparable objects
 */
fun <E : Comparable<E>> minMax(vararg items: E): Pair<E, E> {
    var min = items.first()
    var max = items.first()
    items.forEach { if (it < min) min = it else if (it > max) max = it }
    return min to max
}
