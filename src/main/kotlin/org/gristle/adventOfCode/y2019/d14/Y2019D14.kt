package org.gristle.adventOfCode.y2019.d14

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.y2019.d14.Y2019D14.Chemical.Companion.lookup
import kotlin.math.max

class Y2019D14(private val input: String) {
    private val pattern = """(\d+) ([A-Z]+)""".toRegex()

    data class Chemical(val name: String, val quantity: Long) {
        companion object {
            val lookup = mutableMapOf<String, MutableSet<Chemical>>()
        }
        val upstream: Set<Chemical> by lazy {
            val upSet = mutableSetOf<Chemical>()
            val immediateUp = lookup[name] ?: return@lazy emptySet<Chemical>()
            upSet.addAll(immediateUp)
            for (chem in immediateUp) {
                upSet.addAll(chem.upstream)
            }
            upSet
        }
    }
    data class Reaction(val result: Chemical, val ingredients: List<Chemical>)

    fun solve(): Pair<Long, Long> {
        val rules = buildMap {
            input
                .lines()
                .map { line ->
                    line
                        .groupValues(pattern)
                        .let { gvs ->
                            val result = Chemical(gvs.last()[1], gvs.last()[0].toLong())
                            val ingredients = gvs.dropLast(1).map { gv ->
                                Chemical(gv[1], gv[0].toLong())
                            }
                            val reaction = Reaction(result, ingredients)
                            for (ingredient in reaction.ingredients) {
                                if (lookup[ingredient.name] == null) {
                                    lookup[ingredient.name] = mutableSetOf()
                                }
                                lookup.getValue(ingredient.name).add(result)
                            }
                            put(result.name, reaction)
                        }
                }
        }

        fun calculateOre(fuel: Long): Long {
            val repository = mutableMapOf("FUEL" to fuel)
            var potentials = repository.entries.filter { it.value != 0L }
            var ore = 0L

            while (potentials.isNotEmpty()) {

                val potential = potentials.find { pot ->
                    rules.getValue(pot.key).result.upstream.all { chem ->
                        chem.name !in potentials.map { it.key }
                    }
                } ?: break

                val rule = rules.getValue(potential.key)
                val timesApplied = (potential.value / rule.result.quantity) +
                        if (potential.value % rule.result.quantity > 0) 1 else 0
                repository[potential.key] = max(
                    0,
                    repository.getValue(potential.key) - rule.result.quantity * timesApplied
                )
                for (ingredient in rule.ingredients) {
                    repository[ingredient.name] =
                        (repository[ingredient.name] ?: 0) + ingredient.quantity * timesApplied
                }

                ore += repository["ORE"] ?: 0L
                repository["ORE"] = 0L
                potentials = repository.entries.filter { it.value != 0L }
            }
            return ore
        }
        val oneFuel = calculateOre(1)

        // Part 2
        val totalOre = 1_000_000_000_000L
        val lowerBound = totalOre / oneFuel
        val test = calculateOre(lowerBound)
        val guess = (lowerBound * totalOre) / test
        return oneFuel to guess
    }

}

fun main() {
    var time = System.nanoTime()
    val c = Y2019D14(readRawInput("y2019/d14"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    val (p1, p2) = c.solve()
    println("Part 1: $p1") // 751038
    println("Part 2: $p2 (${elapsedTime(time)}ms)") // 2074843
}