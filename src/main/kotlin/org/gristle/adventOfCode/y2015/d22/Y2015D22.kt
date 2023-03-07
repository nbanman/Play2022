package org.gristle.adventOfCode.y2015.d22

import org.gristle.adventOfCode.Day
import java.util.*

class Y2015D22(input: String) : Day {

    private val stats = input.lines().map { line -> line.takeLastWhile { it.isDigit() }.toInt() }

    private val bossHP = stats[0]
    private val damage = stats[1]

    class Effect(val name: String, val mana: Int, val duration: Int)

    data class State(
        val playerHP: Int,
        val damage: Int,
        val alreadyDead: Boolean,
        val bossHP: Int,
        val currentMana: Int,
        val manaSpent: Int = 0,
        val shield: Int = 0,
        val poison: Int = 0,
        val recharge: Int = 0
    ): Comparable<State> {
        private val armor = if (shield != 0) 7 else 0

        val availableMana = currentMana + (if (recharge != 0) 101 else 0)

        fun cast(spell: Effect, constantDrain: Boolean): State {
            val newDead = constantDrain && playerHP == 1
            val newPlayerHP = playerHP + if (spell.name == "drain") 2 else 0 - if (constantDrain) 1 else 0
            val newBossHP = bossHP +
                    (if (poison > 0) -3 else 0) +
                    (if (spell.name == "magic missile") -4 else 0) +
                    (if (spell.name == "drain") -2 else 0)
            val newCurrentMana = currentMana - (spell.mana) +
                    (if (recharge > 0) 101 else 0)
            val newManaSpent = manaSpent + spell.mana
            val newShield = if (spell.name == "shield") spell.duration else maxOf(0, shield - 1)
            val newPoison = if (spell.name == "poison") spell.duration else maxOf(0, poison - 1)
            val newRecharge = if (spell.name == "recharge") spell.duration else maxOf(0, recharge - 1)
            return State(
                newPlayerHP, damage, newDead, newBossHP, newCurrentMana,
                newManaSpent, newShield, newPoison, newRecharge
            )
        }

        fun bossTurn(): State {
            val newPlayerHP = playerHP - maxOf(1, damage - armor)
            val newBossHP = bossHP + if (poison > 0) -3 else 0
            val newCurrentMana = currentMana + (if (recharge > 0) 101 else 0)
            val newShield = maxOf(0, shield - 1)
            val newPoison = maxOf(0, poison - 1)
            val newRecharge = maxOf(0, recharge - 1)
            return State(
                newPlayerHP, damage, alreadyDead, newBossHP, newCurrentMana,
                manaSpent, newShield, newPoison, newRecharge
            )
        }

        override fun compareTo(other: State) = playerHP - other.playerHP
    }

    private val spells = listOf(
        Effect("magic missile", 53, 1),
        Effect("drain", 73, 1),
        Effect("shield", 113, 6),
        Effect("poison", 173, 6),
        Effect("recharge", 229, 5)
    )

    private fun solve(constantDrain: Boolean): Int {
        val states = PriorityQueue<State>()
        states.add(State(50, damage, false, bossHP, 500))

        var lowestManaWin = Int.MAX_VALUE

        while (states.isNotEmpty()) {
            // player turn
            val current = states.poll()
            spells.filter {
                val alreadyCast = when (it.name) {
                    "shield" -> current.shield > 1
                    "poison" -> current.poison > 1
                    "recharge" -> current.recharge > 1
                    else -> false
                }
                !alreadyCast && current.availableMana >= it.mana
            }.map {
                current.cast(it, constantDrain).bossTurn()
            }.filter {
                when {
                    it.alreadyDead -> false
                    it.bossHP <= 0 -> {
                        lowestManaWin = minOf(lowestManaWin, it.manaSpent)
                        false
                    }
                    it.playerHP <= 0 || it.availableMana < 53 || it.manaSpent > lowestManaWin -> false
                    else -> true
                }
            }.let { states.addAll(it) }
        }
        return lowestManaWin
    }

    override fun part1() = solve(false)

    override fun part2() = solve(true)
}

fun main() = Day.runDay(Y2015D22::class)

//    Class creation: 18ms
//    Part 1: 1824 (379ms)
//    Part 2: 1937 (80ms)
//    Total time: 478ms