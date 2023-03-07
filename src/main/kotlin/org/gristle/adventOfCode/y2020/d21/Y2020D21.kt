package org.gristle.adventOfCode.y2020.d21

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues

class Y2020D21(input: String) : Day {

    data class Food(val ingredients: Set<String>, val allergens: Set<String>) {
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
                lookup.getOrPut(allergen) { mutableSetOf(*ingredients.toTypedArray()) }
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

    override fun part1() = foods.flatMap(Food::ingredients).filterNot(Food.allergenMap.values::contains).size

    override fun part2() = Food.allergenMap.entries.sortedBy { it.key }.joinToString(",") { it.value }
}

fun main() = Day.runDay(Y2020D21::class)

//    Class creation: 26ms
//    Part 1: 2493 (1ms)
//    Part 2: kqv,jxx,zzt,dklgl,pmvfzk,tsnkknk,qdlpbt,tlgrhdh (1ms)
//    Total time: 29ms