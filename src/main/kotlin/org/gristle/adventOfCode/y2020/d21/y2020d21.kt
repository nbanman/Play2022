package org.gristle.adventOfCode.y2020.d21

import org.gristle.adventOfCode.utilities.*

object Y2020D21 {
    private val input = readRawInput("y2020/d21")

    data class Food (val ingredients: Set<String>, val allergens: Set<String>) {
        companion object {
            private val lookup = mutableMapOf<String, MutableSet<String>>()

            val allergenMap: Map<String, String> by lazy {
                val internalLookup = lookup.toMutableMap()
                val aMap = mutableMapOf<String, String>()
                while (internalLookup.isNotEmpty()) {
                    val (singletons, multitons) = internalLookup.entries.partition { it.value.size == 1 }
                    singletons.forEach { singleton ->
                        internalLookup.remove(singleton.key)
                        aMap[singleton.key] = singleton.value.first()
                        multitons.forEach { it.value.remove(singleton.value.first()) }
                    }
                }
                aMap
            }
        }

        init {
            allergens.forEach { allergen ->
                lookup.computeIfAbsent(allergen) { mutableSetOf(*ingredients.toTypedArray()) }
                lookup[allergen] = lookup[allergen]!!.intersect(ingredients).toMutableSet()
            }
        }
    }

    private val foods = input
        .groupValues("""((?:[a-z]+ )+)\(contains ([^)]+)\)""")
        .map { gv ->
            val ingredients = gv[0].split(' ').dropLast(1).toSet()
            val allergens = gv[1].split(", ").toSet()
            Food(ingredients, allergens)
        }

    fun part1() = foods.flatMap { it.ingredients }.filter { !Food.allergenMap.values.contains(it) }.size

    fun part2() = Food.allergenMap.entries.sortedBy { it.key }.joinToString(",") { it.value }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D21.part1()} (${elapsedTime(time)}ms)") // 2493
    time = System.nanoTime()
    println("Part 2: ${Y2020D21.part2()} (${elapsedTime(time)}ms)") // kqv,jxx,zzt,dklgl,pmvfzk,tsnkknk,qdlpbt,tlgrhdh
}