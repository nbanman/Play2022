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
 * Returns the minimum and maximum of a pair of the same comparable object in that order
 */
fun <E : Comparable<E>> Pair<E, E>.minMax() = if (first <= second) first to second else second to first

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

fun <E, R : Comparable<R>> Iterable<E>.minMaxBy(selector: (E) -> R): Pair<E, E> {
    var min = first() to selector(first())
    var max = last() to selector(last())
    forEach {
        val selected = selector(it)
        if (selected < min.second) min = it to selected else if (selected > max.second) max = it to selected
    }
    return min.first to max.first
}

fun <E, R : Comparable<R>> minMaxBy(vararg items: E, selector: (E) -> R): Pair<E, E> {
    var min = items.first() to selector(items.first())
    var max = items.last() to selector(items.last())
    items.forEach {
        val selected = selector(it)
        if (selected < min.second) min = it to selected else if (selected > max.second) max = it to selected
    }
    return min.first to max.first
}