package org.gristle.adventOfCode.utilities

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

/**
 * Splits file by newline
 */
fun String.lines() = split("\n", "\r\n")

/**
 * Splits file by newline and applies transform
 */
inline fun <E> String.lines(transform: (String) -> E) = split("\n", "\r\n").map(transform)

/**
 * Split on predicate
 */
inline fun <T> Iterable<T>.splitOn(predicate: (T) -> Boolean): List<List<T>> {
    val d = mutableListOf<List<T>>()
    val u = mutableListOf<T>()
    for (i in this) {
        if (predicate(i)) {
            d += u
            u.clear()
        } else {
            u.add(i)
        }
    }
    d += u
    return d;
}