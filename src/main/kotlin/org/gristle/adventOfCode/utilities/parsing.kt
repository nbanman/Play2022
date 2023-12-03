@file:Suppress("unused")

package org.gristle.adventOfCode.utilities

import kotlin.reflect.KFunction

/**
 * Finds all numbers in a string and returns them as a Sequence of a number.
 */
//inline fun <N : Number> String.getNumbers(crossinline transform: String.() -> N?): Sequence<N> =
//    Regex("""(?<!\d)-?\d+""")
//        .findAll(this)
//        .mapNotNull { it.value.transform() }


inline fun <N : Number> String.getNumbers(crossinline transform: String.() -> N?): Sequence<N> = sequence {
    var startPosition = -1
    for (position in indices) {
        val c = this@getNumbers[position]
        if (c.isDigit() || (c == '-' && this@getNumbers.getOrNull(position - 1)?.isDigit() != true)) {
            if (startPosition == -1) startPosition = position
        } else {
            if (startPosition != -1) {
                substring(startPosition, position)
                    .transform()
                    ?.let { yield(it) }
                startPosition = -1
            }
        }
    }
    if (startPosition != -1) {
        substring(startPosition)
            .transform()
            ?.let { yield(it) }
    }
}

/**
 * Finds all numbers in a string and returns them as a Sequence of Int.
 */
fun String.getInts(): Sequence<Int> = getNumbers(String::toIntOrNull)

/**
 * Finds all numbers in a string and returns them as a List of Int.
 */
fun String.getIntList() = getInts().toList()

/**
 * Finds all numbers in a string and returns them as a Sequence of Long.
 */
fun String.getLongs(): Sequence<Long> = getNumbers(String::toLongOrNull)

/**
 * Finds all numbers in a string and returns them as a List of Long.
 */
fun String.getLongList() = getLongs().toList()

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
        .map { it.groupValues.drop(1) }
        .toList()
}

/**
 * Takes a regex, parses the fields to supplied class and returns a sequence of instances
 */
fun <E : Any> String.parse(
    kFun: KFunction<E>,
    matchPattern: Regex,
    splitPattern: Regex? = null
): Sequence<E> = matchPattern
    .findAll(this)
    .map { it.groupValues.drop(1).parseToObject(kFun, splitPattern) }

fun <E : Any> String.parse(
    kFun: KFunction<E>,
    matchPattern: String,
    splitPattern: Regex? = null
): Sequence<E> = matchPattern
    .toRegex()
    .findAll(this)
    .map { it.groupValues.drop(1).parseToObject(kFun, splitPattern) }

fun <E : Any> String.parseToList(
    kFun: KFunction<E>,
    matchPattern: Regex,
    splitPattern: Regex? = null
): List<E> = parse(kFun, matchPattern, splitPattern).toList()

fun <E : Any> String.parseToList(
    kFun: KFunction<E>,
    matchPattern: String,
    splitPattern: Regex? = null
): List<E> = parse(kFun, matchPattern, splitPattern).toList()

/**
 * Convenience method to obtain the group values of a findall regex search of a string,
 * with a way to map the strings to something else if they are all the same type.
 */
inline fun <R> String.groupValues(pattern: String, transform: (String) -> R): List<List<R>> =
    groupValues(pattern.toRegex(), transform)

/**
 * Convenience method to obtain the group values of a findall regex search of a string,
 * with a way to map the strings to something else if they are all the same type.
 */
inline fun <R> String.groupValues(pattern: Regex, transform: (String) -> R): List<List<R>> {
    return pattern
        .findAll(this)
        .toList()
        .map { it.groupValues.drop(1).map(transform) }
}

fun String.gvs(regex: Regex): Sequence<List<String>> = regex
    .findAll(this)
    .map { it.groupValues.drop(1) }

fun String.gvs(pattern: String): Sequence<List<String>> = Regex(pattern)
    .findAll(this)
    .map { it.groupValues.drop(1) }

fun <R> String.gvs(regex: Regex, transform: (String) -> R): Sequence<List<R>> = regex
    .findAll(this)
    .map { it.groupValues.drop(1).map(transform) }

fun <R> String.gvs(pattern: String, transform: (String) -> R): Sequence<List<R>> = Regex(pattern)
    .findAll(this)
    .map { it.groupValues.drop(1).map(transform) }

fun String.blankSplit(): List<String> = split("\n\n")

/**
 * Splits file by newline and applies transform
 */
inline fun <E> String.lines(transform: (String) -> E) = lines().map(transform)

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
    return d
}

/**
 * Split on blank String
 */
fun <String> Iterable<String>.splitOnBlank(): List<List<String>> {
    val d = mutableListOf<List<String>>()
    var u = mutableListOf<String>()
    forEach { s ->
        if (s == "") {
            d += u
            u = mutableListOf()
        } else {
            u.add(s)
        }
    }
    d += u
    return d
}
