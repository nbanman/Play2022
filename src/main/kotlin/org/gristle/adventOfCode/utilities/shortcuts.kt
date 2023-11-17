package org.gristle.adventOfCode.utilities

import java.util.*
import kotlin.math.min
import kotlin.math.pow

/**
 * Prints to console if the Boolean is true. Used for quick and dirty debugging.
 */
fun Boolean.print(s: String) {
    if (this) println(s)
}

/**
 * Returns an Int from a BooleanArray
 */
fun BooleanArray.toInt() = foldIndexed(0) { idx, acc, b ->
    if (b) {
        acc + (1 shl (size - idx - 1))
    } else {
        acc
    }
}

/**
 * Returns the actual Int digit (not the ASCII code) of the numeric Char; -1 if not numeric.
 */
fun Char.toDigit() = Character.getNumericValue(this)

/**
 * Shortcut for groupingBy { it }.eachCount()
 */
fun CharSequence.eachCount() = groupingBy { it }.eachCount()

/**
 * Prints contents of a collection line by line
 */
fun <E> Collection<E>.testPrint(transform: (E) -> String = { e -> e.toString() }) =
    if (isEmpty()) println("Empty collection.") else forEach { println(transform(it)) }

/**
 * Returns status of whether Int is even
 */
fun Int.isEven(): Boolean = this % 2 == 0

/**
 * Returns status of whether Int is odd
 */
fun Int.isOdd(): Boolean = this % 2 == 1

/**
 * pow implementation for Int
 */
fun Int.pow(n: Int): Long = if (n >= 0) {
    (1..n).fold(1L) { acc, _ -> acc * this }
} else {
    this.toFloat().pow(n).toLong()
}

/**
 * Reverse the bits in an integer
 */
fun Int.reversed(bits: Int): Int {
    var input = this
    var output = 0
    var counter = 0
    while (input > 0) {
        val bit = 1.and(input)
        input = input.shr(1)
        output = output.shl(1) + bit
        counter++
    }
    output = output.shl(bits - counter)
    return output
}

/**
 * Returns true if an IntRange is a superset of another IntRange; otherwise false.
 */
fun IntRange.containsAll(other: IntRange): Boolean =
    first <= other.first && last >= other.last

/**
 * Returns true if two IntRanges overlap; otherwise false.
 */
fun IntRange.overlaps(other: IntRange): Boolean = if (first <= other.first) {
    last >= other.first
} else {
    other.last >= first
}

/**
 * Shortcut for groupingBy { it }.eachCount()
 */
fun <E> Iterable<E>.eachCount() = groupingBy { it }.eachCount()

/**
 * Splits the original collection into n lists, interleaving elements among the lists.
 */
fun <T> Iterable<T>.collate(threads: Int): List<List<T>> {
    val partitions = List(threads) { ArrayList<T>() }
    forEachIndexed { index, element -> partitions[index % threads].add(element) }
    return partitions
}

/**
 * Splits the original sequence into n lists, interleaving elements among the lists.
 */
fun <T> Sequence<T>.collate(threads: Int): Sequence<List<T>> {
    val partitions = List(threads) { ArrayList<T>() }
    forEachIndexed { index, element -> partitions[index % threads].add(element) }
    return partitions.asSequence()
}

/**
 * Splits the original collection into a pair of lists, where first list contains elements for which predicate yielded
 * true, while second list contains elements for which predicate yielded false. Exposes an index value.
 */

inline fun <T> Iterable<T>.partitionIndexed(predicate: (index: Int, T) -> Boolean): Pair<List<T>, List<T>> {
    val first = ArrayList<T>()
    val second = ArrayList<T>()
    forEachIndexed { index, element ->
        if (predicate(index, element)) {
            first.add(element)
        } else {
            second.add(element)
        }
    }
    return Pair(first, second)
}

/**
 * Splits the original string into a pair of strings, where first string contains elements for which predicate yielded
 * true, while second string contains elements for which predicate yielded false. Exposes an index value.
 */
inline fun String.partitionIndexed(predicate: (index: Int, Char) -> Boolean): Pair<String, String> {
    val first = StringBuilder()
    val second = StringBuilder()
    forEachIndexed { index, element ->
        if (predicate(index, element)) {
            first.append(element)
        } else {
            second.append(element)
        }
    }
    return Pair(first.toString(), second.toString())
}

fun <E : Comparable<E>> Iterable<E>.toPriorityQueue(): PriorityQueue<E> {
    val pq = if (this is Collection<E>) {
        PriorityQueue<E>(size)
    } else {
        PriorityQueue<E>()
    }
    return pq.also { it.addAll(this) }
}

inline fun <E, R : Comparable<R>> Iterable<E>.toPriorityQueue(transform: (E) -> R): PriorityQueue<R> {
    val pq = if (this is Collection<E>) {
        PriorityQueue<R>(size)
    } else {
        PriorityQueue<R>()
    }
    return pq.also {
        forEach { e ->
            pq.add(transform(e))
        }
    }
}

fun <E : Comparable<E>> Iterable<E>.toPriorityQueueDescending(): PriorityQueue<E> {
    val pq = if (this is Collection<E>) {
        PriorityQueue(size, compareByDescending { e: E -> e })
    } else {
        PriorityQueue(compareByDescending { e: E -> e })
    }
    return pq.also { it.addAll(this) }
}

inline fun <E, R : Comparable<R>> Iterable<E>.toPriorityQueueDescending(transform: (E) -> R): PriorityQueue<R> {
    val pq = if (this is Collection<E>) {
        PriorityQueue(size, compareByDescending { r: R -> r })
    } else {
        PriorityQueue(compareByDescending { r: R -> r })
    }
    return pq.also {
        forEach { e ->
            pq.add(transform(e))
        }
    }
}

/**
 * Shifts the start index of the list by n. The skipped parts get wrapped to the end. Accepts
 * negative numbers to go in the reverse direction.
 */
fun <E> List<E>.shift(n: Int): List<E> {
    val newIndex = n.mod(size)
    return drop(newIndex) + subList(0, newIndex)
}

inline fun <E> MutableList<E>.mapInPlace(transform: (E) -> E) = apply {
    forEachIndexed { index, element -> this[index] = transform(element) }
}

inline fun <E> MutableList<E>.mapInPlaceIndexed(transform: (index: Int, E) -> E) = apply {
    forEachIndexed { index, element -> this[index] = transform(index, element) }
}

/**
 * Transposes a list of lists to become a <I>list</I> of <I>lists</I>. If the lists are of unequal length it uses the
 * length of the shortest list.
 */
fun <E> List<List<E>>.transpose(): List<List<E>> {
    val width = minOf { it.size }
    val height = size
    return List(width) { w ->
        List(height) { h ->
            this[h][w]
        }
    }
}

/**
 * pow implementation for Long
 */
fun Long.pow(n: Int): Long = if (n >= 0) {
    (1..n).fold(1L) { acc, _ -> acc * this }
} else {
    this.toFloat().pow(n).toLong()
}

/**
 * For maps using Int as a value, get value for key or 0 if no matching key found.
 */
fun <K> Map<K, Int>.get0(key: K) = get(key) ?: 0

/**
 * Used in algorithms that have multiple nodes with different weights representing the same location.
 * This way, nodes that have already been visited can be skipped.
 */
inline fun <E> PriorityQueue<E>.pollUntil(predicate: (E) -> Boolean): E? {
    var poll = poll()
    while (poll != null) {
        if (predicate(poll)) return poll
        poll = poll()
    }
    return null
}

/**
 * Calls poll specified number of times, returns results as a list.
 */
fun <E> PriorityQueue<E>.poll(n: Int): List<E> {
    require(n > 0) { "n must be a positive Integer" }
    return MutableList(min(n, size)) { poll() }
}

/**
 * Calls poll until the predicate is no longer met or the PriorityQueue is empty.
 */
inline fun <E> PriorityQueue<E>.pollWhile(predicate: (E) -> Boolean): List<E> {
    val list = mutableListOf<E>()
    while (isNotEmpty()) {
        poll().let { if (predicate(it)) list.add(it) }
    }
    return list
}

