package org.gristle.adventOfCode.y2015.d15

import org.gristle.adventOfCode.utilities.*

object Y2015D15 {
    private val input = readRawInput("y2015/d15")

    data class Ingredient(
        val name: String,
        val capacity: Int,
        val durability: Int,
        val flavor: Int,
        val texture: Int,
        val calories: Int
    )

    const val PATTERN = """(\w+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (\d+)"""

    val ingredients = input
        .groupValues(PATTERN)
        .map { gv ->
            val ints = gv.drop(1).map { it.toInt() }
            Ingredient(gv[0], ints[0], ints[1], ints[2], ints[3], ints[4])
        }

    fun meetsCalories(combo: List<Int>, ingredients: List<Ingredient>, calories: Int): Boolean {
        var calorieCount = 0
        for (i in combo.indices) {
            calorieCount += combo[i] * ingredients[i].calories
        }
        return calories == calorieCount
    }

    fun comboScore(combo: List<Int>, ingredients: List<Ingredient>): Int {
        var capacity: Int = 0
        var durability: Int = 0
        var flavor: Int = 0
        var texture: Int = 0

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

    fun getCombos(ingredientNum: Int, total: Int): List<List<Int>> {
        tailrec fun gC(combos: List<List<Int>>): List<List<Int>> {
            return if (combos.first().size < ingredientNum) {
                val newCombos = combos.foldToList { combo ->
                    val currentSum = combo.sum()
                    val remaining = total - currentSum
                    if (combos.first().size == ingredientNum - 1) {
                        addAll(listOf((combo + listOf(remaining))))
                    } else {
                        addAll((0..remaining).map { combo + listOf(it) })
                    }
                }
                gC(newCombos)
            } else {
                combos
            }
        }
        val seed = (0..total).map { listOf(it) }
        return gC(seed)
    }

    val total = 100
    val ingredientNum = ingredients.size
    val calories = 500
    val combos = getCombos(ingredientNum, total)

    fun part1() = comboScore(
        combos.maxByOrNull { comboScore(it, ingredients) }!!,
        ingredients
    )

    fun part2() = comboScore(
        combos
            .filter { meetsCalories(it, ingredients, calories) }
            .maxByOrNull { comboScore(it, ingredients) }!!,
        ingredients
    )
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D15.part1()} (${elapsedTime(time)}ms)") // 222870
    time = System.nanoTime()
    println("Part 2: ${Y2015D15.part2()} (${elapsedTime(time)}ms)") // 117936
}