package org.gristle.adventOfCode.utilities

inline fun <T, R> Iterable<T>.foldToList(operation: MutableList<R>.(T) -> Unit): List<R> {
    val list = mutableListOf<R>()
    forEach { element -> list.operation(element) }
    return list
}
