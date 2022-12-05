package org.gristle.adventOfCode.utilities

/**
 * Finds all numbers in a string and returns them as a List of Int.
 */
fun String.getIntList(omitDashes: Boolean = false): List<Int> {
    val pattern = if (omitDashes) """\d+""" else """([+\-])?\d+"""
    return pattern
        .toRegex()
        .findAll(this)
        .mapNotNull { it.value.toIntOrNull() }
        .toList()
}


/**
 * Finds all numbers in a string and returns them as a Sequence of Int.
 */
fun String.getInts(omitDashes: Boolean = false): Sequence<Int> {
    val pattern = if (omitDashes) """\d+""" else """([+\-])?\d+"""
    return pattern
        .toRegex()
        .findAll(this)
        .mapNotNull { it.value.toIntOrNull() }
}

/**
 * Convenience method to obtain the group values of a findall regex search of a string.
 */
fun String.groupValues(pattern: String): List<List<String>> = groupValues(pattern.toRegex())

/**
 * Convenience method to obtain the group values of a findall regex search of a string.
 */
fun String.groupValues(pattern: Regex): List<List<String>> {
    return pattern
        .findAll(this)
        .toList()
        .map { it.groupValues.drop(1) }
}

/**
 * Convenience method to obtain the group values of a findall regex search of a string,
 * with a way to map the strings to something else if they are all of the same type.
 */
inline fun <R> String.groupValues(pattern: String, transform: (String) -> R): List<List<R>> =
    groupValues(pattern.toRegex(), transform)

/**
 * Convenience method to obtain the group values of a findall regex search of a string,
 * with a way to map the strings to something else if they are all of the same type.
 */
inline fun <R> String.groupValues(pattern: Regex, transform: (String) -> R): List<List<R>> {
    return pattern
        .findAll(this)
        .toList()
        .map { it.groupValues.drop(1).map(transform) }
}

fun String.blankSplit(): List<String> = split("\n\n")

/**
 * Splits file by newline and applies transform
 */
inline fun <E> String.lines(transform: (String) -> E) = split("\n", "\r\n").map(transform)

fun List<String>.transpose(): List<String> {
    val width = first().length
    require(all { it.length == width }) { "The rows are not of equal size." }
    val height = size
    return List(width) { x ->
        buildString {
            for (y in 0 until height) {
                append(this@transpose[y][x])
            }
        }
    }
}

/**
 * Split on predicate
 */
inline fun <T> Iterable<T>.splitOn(predicate: (T) -> Boolean): List<List<T>> {
    val d = mutableListOf<List<T>>()
    var u = mutableListOf<T>()
    forEach { t ->
        if (predicate(t)) {
            d += u
            u = mutableListOf()
        } else {
            u.add(t)
        }
    }
    d += u
    return d;
}

