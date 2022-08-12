package org.gristle.adventOfCode.utilities

/**
 * Convenience method to obtain the group values of a findall regex search of a string.
 */
fun String.groupValues(pattern: String): List<List<String>> {
    return pattern
        .toRegex()
        .findAll(this)
        .toList()
        .map { it.groupValues.drop(1) }
}

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
fun <R> String.groupValues(pattern: String, transform: (String) -> R): List<List<R>> {
    return groupValues(pattern)
        .map { matchList ->
            matchList.map {transform(it)}
        }
}

/**
 * Convenience method to obtain the group values of a findall regex search of a string,
 * with a way to map the strings to something else if they are all of the same type.
 */
fun <R> String.groupValues(pattern: Regex, transform: (String) -> R): List<List<R>> {
    return groupValues(pattern)
        .map { matchList ->
            matchList.map {transform(it)}
        }
}

/**
 * Splits file by newline
 */
fun String.lines() = split("\n", "\r\n")

