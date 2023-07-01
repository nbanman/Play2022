package org.gristle.adventOfCode.utilities

fun <T> Iterable<T>.getPermutations(seed: List<T> = emptyList()): List<List<T>> {
    tailrec fun permute(combos: List<List<T>>, drawPile: List<T>): List<List<T>> {
        return if (combos.first().size == drawPile.size + seed.size) {
            combos
        } else {
            val newCombos = buildList {
                combos.forEach { combo -> addAll((drawPile - combo.toSet()).map { combo + it }) }
            }
            permute(newCombos, drawPile)
        }
    }
    val asList = this.toList() - seed.toSet()
    return permute(asList.map { seed + it }, asList)
}

fun <T> Iterable<T>.getPermutations(seed: List<T> = emptyList(), pruning: (List<T>, T) -> Boolean): List<List<T>> {
    tailrec fun permute(combos: List<List<T>>, drawPile: List<T>): List<List<T>> {
        return if (combos.first().size == drawPile.size + seed.size) {
            combos
        } else {
            val newCombos = buildList {
                combos.forEach { combo ->
                    addAll((drawPile - combo.toSet()).mapNotNull { if (pruning(combo, it)) null else combo + it })
                }
            }
            permute(newCombos, drawPile)
        }
    }

    val asList = this.toList() - seed.toSet()
    return permute(asList.map { if (pruning(emptyList(), it)) seed else seed + it }, asList)
}

fun <E> List<E>.getPairs(): List<List<E>> {
    val combos = mutableListOf<List<E>>()
    for (i in 0 until lastIndex) for (j in i + 1..lastIndex) combos.add(listOf(this[i], this[j]))
    return combos
}

fun <E> List<E>.getPairSequence(): Sequence<List<E>> = sequence {
    for (i in 0 until lastIndex) for (j in i + 1..lastIndex) {
        yield(listOf(this@getPairSequence[i], this@getPairSequence[j]))
    }
}