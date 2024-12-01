package org.gristle.adventOfCode.ec

import org.gristle.adventOfCode.utilities.Indexer
import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.minMax

fun main() {
    val (input1, input2, input3) = ecInputs(24, 11)
    val timer = Stopwatch(true)
    println("1. ${getPopulation(input1, 4, "A")}: ${timer.lap()}ms")
    println("2. ${getPopulation(input2, 10, "Z")}: ${timer.lap()}ms")
    println("3. ${minmaxPopulation(input3)}: ${timer.lap()}ms")
    println("Total: ${timer.stop()}ms")
}

private fun getPopulation(input: String, days: Int, start: String): Long {
    val generations = getGenerations(input, start)
    val population = LongArray(generations.size)
    population[0] = 1
    return breed(population, generations, days)
}

fun minmaxPopulation(input: String): Long {
    val generations = getGenerations(input)
    val (min, max) = generations.indices
        .map { termite ->
            val population = LongArray(generations.size)
            population[termite] = 1
            breed(population, generations, 20)
        }.minMax()
    return max - min
}

fun getGenerations(input: String, start: String? = null): List<List<Int>> {
    val indexer = Indexer<String>()
    start?.let { indexer.put(it) }
    val generations: List<List<Int>> = input.lines()
        .map { line ->
            val (prev, next) = line.split(':')
            val id = indexer.getOrPut(prev)
            val children = next
                .split(',')
                .map { child -> indexer.getOrPut(child) }
            id to children
        }.sortedBy { (id, _) -> id }
        .map { (_, children) -> children }
    return generations
}

private fun breed(population: LongArray, generations: List<List<Int>>, days: Int): Long =
    generateSequence(population) { nextGen(it, generations) }
        .take(days + 1)
        .last()
        .sum()

private fun nextGen(population: LongArray, generations: List<List<Int>>): LongArray {
    val nextGen = LongArray(population.size)
    for ((termite, amt) in population.withIndex()) {
        val offspring: List<Int> = generations[termite]
        for (child in offspring) {
            nextGen[child] += amt
        }
    }
    return nextGen
}