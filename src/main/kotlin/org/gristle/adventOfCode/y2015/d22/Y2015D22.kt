package org.gristle.adventOfCode.y2015.d22

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList
import java.util.*

class Y2015D22(input: String) : Day {

    private val bossHP: Int
    private val damage: Int

    init {
        val (bhp, dam) = input.getIntList()
        bossHP = bhp
        damage = dam
    }

    sealed interface Spell {
        val mana: Int
        val duration: Int
    }

    object MagicMissile : Spell {
        override val mana: Int = 53
        override val duration: Int = 1
    }

    object Drain : Spell {
        override val mana: Int = 73
        override val duration: Int = 1
    }

    object Shield : Spell {
        override val mana: Int = 113
        override val duration: Int = 6
    }

    object Poison : Spell {
        override val mana: Int = 173
        override val duration: Int = 6
    }

    object Recharge : Spell {
        override val mana: Int = 229
        override val duration: Int = 5
    }

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

        fun cast(spell: Spell, constantDrain: Boolean): State {
            val newDead = constantDrain && playerHP == 1
            val newPlayerHP = playerHP + if (spell is Drain) 2 else 0 - if (constantDrain) 1 else 0
            val newBossHP = bossHP +
                    (if (poison > 0) -3 else 0) +
                    (if (spell is MagicMissile) -4 else 0) +
                    (if (spell is Drain) -2 else 0)
            val newCurrentMana = currentMana - (spell.mana) +
                    (if (recharge > 0) 101 else 0)
            val newManaSpent = manaSpent + spell.mana
            val newShield = if (spell is Shield) spell.duration else maxOf(0, shield - 1)
            val newPoison = if (spell is Poison) spell.duration else maxOf(0, poison - 1)
            val newRecharge = if (spell is Recharge) spell.duration else maxOf(0, recharge - 1)
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

        override fun compareTo(other: State) = other.manaSpent - manaSpent
    }

    private fun solve(constantDrain: Boolean): Int {
        val spells = listOf(MagicMissile, Drain, Shield, Poison, Recharge)
        val states = PriorityQueue<State>()
        states.add(State(50, damage, false, bossHP, 500))

        var lowestManaWin = Int.MAX_VALUE

        while (states.isNotEmpty()) {
            // player turn
            val current = states.poll()
            spells.filter { spell ->
                val alreadyCast = when (spell) {
                    is Shield -> current.shield > 1
                    is Poison -> current.poison > 1
                    is Recharge -> current.recharge > 1
                    else -> false
                }
                !alreadyCast && current.availableMana >= spell.mana
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