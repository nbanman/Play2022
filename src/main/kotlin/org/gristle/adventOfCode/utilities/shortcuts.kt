package org.gristle.adventOfCode.utilities

import java.util.*

/**
 * Returns elapsed time in ms.
 */
fun elapsedTime(m: Long) = (System.nanoTime() - m) / 1_000_000

/**
 * Prints to console if the Boolean is true. Used for quick and dirty debugging.
 */
fun Boolean.print(s: String) {
    if (this) println(s)
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
 * Proper mod function.
 */
infix fun Int.fmod(other: Int) = Math.floorMod(this, other)

/**
 * Returns status of whether Int is even
 */
fun Int.isEven(): Boolean = this % 2 == 0

/**
 * Returns status of whether Int is odd
 */
fun Int.isOdd(): Boolean = this % 2 == 1

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
 * Shortcut for groupingBy { it }.eachCount()
 */
fun <E> Iterable<E>.eachCount() = groupingBy { it }.eachCount()

/**
 * Shifts the start index of the list by n. The skipped parts get wrapped to the end. Accepts
 * negative numbers to go in the reverse direction.
 */
fun <E> List<E>.shift(n: Int): List<E> {
    val newIndex = n fmod size
    return drop(newIndex) + subList(0, newIndex)
}

/**
 * Transposes a list of lists to become a <I>list</I> of <I>lists</I>
 */
fun <E> List<List<E>>.transpose(): List<List<E>> {
    val width = first().size
    val height = size
    require (all { it.size == width }) { "The rows are not of equal size." }
    return List(width) { w ->
        List(height) { h ->
            this[h][w]
        }
    }
}

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
 * Takes two Comparable values and returns a pair with the lesser in the first position and the greater in the second
 */
fun <T: Comparable<T>> lesserToGreater(a: T, b: T): Pair<T, T> = if (a <= b) a to b else b to a