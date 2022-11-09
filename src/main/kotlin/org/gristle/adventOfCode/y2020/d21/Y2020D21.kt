package org.gristle.adventOfCode.y2020.d21

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2020D21(input: String) {

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

    fun part1() = foods.flatMap(Food::ingredients).filterNot(Food.allergenMap.values::contains).size

    fun part2() = Food.allergenMap.entries.sortedBy { it.key }.joinToString(",") { it.value }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D21(readRawInput("y2020/d21"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 2493
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // kqv,jxx,zzt,dklgl,pmvfzk,tsnkknk,qdlpbt,tlgrhdh
}