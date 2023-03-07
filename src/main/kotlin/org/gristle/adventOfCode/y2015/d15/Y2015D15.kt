package org.gristle.adventOfCode.y2015.d15

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues

class Y2015D15(input: String) : Day {

    data class Ingredient(
        val name: String,
        val capacity: Int,
        val durability: Int,
        val flavor: Int,
        val texture: Int,
        val calories: Int
    )

    private val pattern =
        """(\w+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (\d+)"""

    private val ingredients = input
        .groupValues(pattern)
        .map { gv ->
            val ints = gv.drop(1).map { it.toInt() }
            Ingredient(gv[0], ints[0], ints[1], ints[2], ints[3], ints[4])
        }

    private val total = 100
    private val ingredientNum = ingredients.size
    private val calories = 500
    private val combos = getCombos()

    private fun meetsCalories(combo: List<Int>): Boolean {
        var calorieCount = 0
        for (i in combo.indices) {
            calorieCount += combo[i] * ingredients[i].calories
        }
        return calories == calorieCount
    }

    private fun comboScore(combo: List<Int>): Int {
        var capacity = 0
        var durability = 0
        var flavor = 0
        var texture = 0

        for (i in combo.indices) {
            capacity += combo[i] * ingredients[i].capacity
            durability += combo[i] * ingredients[i].durability
            flavor += combo[i] * ingredients[i].flavor
            texture += combo[i] * ingredients[i].texture
        }
        capacity = maxOf(capacity, 0)
        durability = maxOf(durability, 0)
        flavor = maxOf(flavor, 0)
        texture = maxOf(texture, 0)

        return capacity * durability * flavor * texture
    }

    private fun getCombos(): List<List<Int>> {
        tailrec fun gC(combos: List<List<Int>>): List<List<Int>> {
            return if (combos.first().size < ingredientNum) {
                val newCombos = buildList {
                    combos.forEach { combo ->
                        val currentSum = combo.sum()
                        val remaining = total - currentSum
                        if (combos.first().size == ingredientNum - 1) {
                            addAll(listOf((combo + listOf(remaining))))
                        } else {
                            addAll((0..remaining).map { combo + listOf(it) })
                        }
                    }
                }
                gC(newCombos)
            } else {
                combos
            }
        }

        val seed = (0..total).map(::listOf)
        return gC(seed)
    }

    override fun part1() = comboScore(combos.maxBy(::comboScore))

    override fun part2() = comboScore(combos.filter(::meetsCalories).maxBy(::comboScore))
}

fun main() = Day.runDay(Y2015D15::class)

//    Class creation: 122ms
//    Part 1: 222870 (16ms)
//    Part 2: 117936 (13ms)
//    Total time: 151ms