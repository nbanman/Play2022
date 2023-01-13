package org.gristle.adventOfCode.utilities

fun <T> Sequence<T>.stabilized() = zipWithNext().first { (prev, next) -> prev == next }.first

fun <T> Sequence<IndexedValue<T>>.stabilized() = zipWithNext()
    .first { (prev, next) -> prev.value == next.value }.first

inline fun <T, U> Sequence<T>.stabilized(selector: (T) -> U) =
    zipWithNext().first { (prev, next) -> selector(prev) == selector(next) }.first

inline fun <T, U> Sequence<IndexedValue<T>>.stabilized(selector: (T) -> U) = zipWithNext()
    .first { (prev, next) -> selector(prev.value) == selector(next.value) }.first

inline fun <T> Sequence<T>.takeUntil(crossinline predicate: (T) -> Boolean): Sequence<T> = sequence {
    for (item in this@takeUntil) {
        yield(item)
        if (predicate(item)) break
    }
}