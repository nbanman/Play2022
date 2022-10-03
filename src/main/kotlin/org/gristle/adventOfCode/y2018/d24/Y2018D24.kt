package org.gristle.adventOfCode.y2018.d24

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2018D24(input: String) {
    private val data = input.split("Infection:\r\n")

    private var boost = 0
    
    private val selectionOrder =
        compareByDescending<ArmyUnit> { it.effectivePower(boost) }.thenByDescending { it.initiative }
    
    data class ArmyUnit(
        val team: String,
        val group: Int,
        var units: Int,
        val hp: Int,
        val immunities: List<String>,
        val weaknesses: List<String>,
        val damage: Int,
        val damageType: String,
        val initiative: Int
    ) {
        fun effectivePower(boost: Int = 0): Int = units * (damage + if (team == "Immune System") boost else 0)

        fun modifiedDamage(other: ArmyUnit, boost: Int = 0): Int = when (other.damageType) {
            in weaknesses -> other.effectivePower(boost) * 2
            in immunities -> 0
            else -> other.effectivePower(boost)
        }

        fun takeDamage(damage: Int) {
            units -= damage / hp
        }

        fun isImmune(other: ArmyUnit): Boolean = other.damageType in immunities
    }

    fun part1(): Int {
        var immuneSystem = makeUnits("Immune System", data.first())
        var infection = makeUnits("Infection", data.last())
        // play rounds
        while (immuneSystem.isNotEmpty() && infection.isNotEmpty()) {
            // target selection phase
            val immuneSelections = selectTargets(immuneSystem.sortedWith(selectionOrder), infection)
            val infectionSelections = selectTargets(infection.sortedWith(selectionOrder), immuneSystem)

            // attack phase
            val attackers = (immuneSelections + infectionSelections).sortedByDescending { it.first.initiative }
            for (attackerDefender in attackers) {
                val attacker = attackerDefender.first
                val defender = attackerDefender.second
                if (attacker.units <= 0) continue
                defender.takeDamage(defender.modifiedDamage(attacker))
            }

            // cleanup phase
            immuneSystem = immuneSystem.filter { it.units > 0 }
            infection = infection.filter { it.units > 0 }
        }

        return (immuneSystem + infection).sumOf { it.units }
    }

    fun part2(): Int {
        // play rounds
        var skipped: Boolean
        loop@do {
            skipped = false
            var immuneSystem = makeUnits("Immune System", data.first())
            var infection = makeUnits("Infection", data.last())
            boost++
            var round = 0
            while(immuneSystem.isNotEmpty() && infection.isNotEmpty()) {

                round++
                val unitSum = immuneSystem.sumOf { it.units } + infection.sumOf { it.units }
//            println("Round $round")
//            println("Immune System:")
//            immuneSystem.forEach { println("Group ${it.group} contains ${it.units} units") }
//            println("Infection:")
//            infection.forEach { println("Group ${it.group} contains ${it.units} units") }
//            println()
                // target selection phase
                val immuneSelections = selectTargets(immuneSystem.sortedWith(selectionOrder), infection, boost)
                val infectionSelections = selectTargets(infection.sortedWith(selectionOrder), immuneSystem, boost)

                // attack phase

                val attackers = (immuneSelections + infectionSelections).sortedByDescending { it.first.initiative }
                for (attackerDefender in attackers) {
                    val attacker = attackerDefender.first
                    val defender = attackerDefender.second
                    if (attacker.units <= 0) continue
//                println("${attacker.team} group ${attacker.group} attacks ${defender.team} group ${defender.group}, killing ${ kotlin.math.min(defender.modifiedDamage(attacker, boost) / defender.hp, defender.units) } units")
                    defender.takeDamage(defender.modifiedDamage(attacker, boost))
                }

                // cleanup phase
                immuneSystem = immuneSystem.filter { it.units > 0 }
                infection = infection.filter { it.units > 0 }
                if (immuneSystem.sumOf { it.units } + infection.sumOf { it.units } == unitSum) {
                    skipped = true
                    continue@loop
                }
            }
            val p2 = (immuneSystem + infection).sumOf { it.units }
            if(infection.isEmpty()) return p2
        } while (immuneSystem.isEmpty() || skipped)

        return -1
    }

    private fun selectTargets(
        attackers: List<ArmyUnit>,
        defenders: List<ArmyUnit>,
        boost: Int = 0
    ): List<Pair<ArmyUnit, ArmyUnit>> {
        val attackerSelections = mutableListOf<Pair<ArmyUnit, ArmyUnit>>()
        attackers.fold(defenders) { acc, attacker ->
            val defender = acc.sortedWith(
                compareByDescending<ArmyUnit>{ it.modifiedDamage(attacker, boost) }
                    .thenByDescending { it.effectivePower(boost) }
                    .thenByDescending { it.initiative }
            )
            if (defender.isEmpty() || defender.first().isImmune(attacker)) {
                acc
            } else {
//            println("${attacker.team} group ${attacker.group} would deal ${defender.first().team} group ${defender.first().group} ${defender.first().modifiedDamage(attacker, boost)} damage")
                attackerSelections.add(attacker to defender.first())
                acc - defender.first()
            }
        }
        return attackerSelections
    }

    private fun makeUnits(team: String, s: String): List<ArmyUnit> = s
        .groupValues(pattern)
        .mapIndexed { index, gv ->
            val units = gv[0].toInt()
            val hp = gv[1].toInt()
            val immunities = patternImmune.find(gv[2])?.groupValues?.get(1)?.split(", ") ?: emptyList()
            val weaknesses = patternWeak.find(gv[2])?.groupValues?.get(1)?.split(", ") ?: emptyList()
            val damage = gv[3].toInt()
            val damageType = gv[4]
            val initiative = gv[5].toInt()
            ArmyUnit(team, index + 1, units, hp, immunities, weaknesses, damage, damageType, initiative)
        }

    private val pattern = """(\d+) units each with (\d+) hit points (?:\(([^)]+)\) )?with an attack that does (\d+) ([a-z]+) damage at initiative (\d+)""".toRegex()
    private val patternWeak = """weak to ((?:[a-z]+(?:, )?)+)""".toRegex()
    private val patternImmune = """immune to ((?:[a-z]+(?:, )?)+)""".toRegex()
    
}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D24(readRawInput("y2018/d24"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 15165
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 4037
}