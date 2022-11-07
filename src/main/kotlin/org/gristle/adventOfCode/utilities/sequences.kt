package org.gristle.adventOfCode.utilities

fun <T> Sequence<T>.stabilized() = zipWithNext().first { (prev, next) -> prev == next }.first

fun <T> Sequence<IndexedValue<T>>.stabilized() = zipWithNext()
    .first { (prev, next) -> prev.value == next.value }.first