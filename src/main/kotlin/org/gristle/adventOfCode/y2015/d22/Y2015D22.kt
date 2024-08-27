package org.gristle.adventOfCode.y2015.d22

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList
import java.util.*

class Y2015D22(input: String) : Day {
    // Process input
    private val bossHP: Int
    private val damage: Int

    init {
        val (bhp, dam) = input.getIntList()
        bossHP = bhp
        damage = dam
    }

    enum class Spell(val mana: Int, val duration: Int, val effect: Int) {
        MagicMissile(53, 1, 4),
        Drain(73, 1, 2),
        Shield(113, 6, 7),
        Poison(173, 6, 3),
        Recharge(229, 5, 101)
    }

    data class State(
        val playerHP: Int,
        val bossHP: Int,
        val currentMana: Int,
        val manaSpent: Int,
        val shield: Int,
        val poison: Int,
        val recharge: Int
    ): Comparable<State> {
        val availableMana = currentMana + (if (recharge > 0) Spell.Recharge.effect else 0)

        fun playerTurn(spell: Spell, constantDrain: Int): State {
            // If player dies from constant drain, boss does not sustain any damage, so return early
            if (playerHP - constantDrain == 0) return copy(playerHP = 0)
            val newPlayerHP = playerHP +
                    (if (spell == Spell.Drain) Spell.Drain.effect else 0) -  
                    constantDrain 
            val newBossHP = bossHP -
                    (if (poison > 0) Spell.Poison.effect else 0) - 
                    (if (spell == Spell.MagicMissile) Spell.MagicMissile.effect else 0) -
                    if (spell == Spell.Drain) Spell.Drain.effect else 0
            val newCurrentMana = currentMana - 
                    spell.mana +
                    if (recharge > 0) Spell.Recharge.effect else 0
            val newManaSpent = manaSpent + spell.mana
            val newShield = if (spell == Spell.Shield) spell.duration else shield - 1
            val newPoison = if (spell == Spell.Poison) spell.duration else poison - 1
            val newRecharge = if (spell == Spell.Recharge) spell.duration else recharge - 1
            return State(
                newPlayerHP, newBossHP, newCurrentMana,
                newManaSpent, newShield, newPoison, newRecharge,
            )
        }

        fun bossTurn(damage: Int): State {
            if (playerHP <= 0) return this
            val armor = if (shield > 0) Spell.Shield.effect else 0
            val newPlayerHP = playerHP - (damage - armor).coerceAtLeast(1)
            val newBossHP = bossHP - if (poison > 0) Spell.Poison.effect else 0
            val newCurrentMana = currentMana + if (recharge > 0) Spell.Recharge.effect else 0
            return State(
                newPlayerHP, newBossHP, newCurrentMana, manaSpent, 
                shield - 1, poison - 1, recharge - 1,
            )
        }

        override fun compareTo(other: State) = other.manaSpent - manaSpent
    }

    /**
     * Dijkstra implementation, maintaining game state and trying all available spells, trying minimum mana states
     * first. 
     */
    private fun solve(constantDrain: Int): Int {
        val states = PriorityQueue<State>()
        states.add(State(50, bossHP, 500, 0, 0, 0, 0))

        var lowestManaWin = Int.MAX_VALUE

        while (states.isNotEmpty()) {
            val current = states.poll()
            
            // try every castable spell and calculate the state
            val nextStates = Spell.entries
                .filter { spell ->
                    if (current.availableMana < spell.mana) {
                        false // not enough mana to cast!
                    } else {
                        // You can only cast if the effect is expired/expiring
                        when (spell) {
                            Spell.Shield -> current.shield <= 1
                            Spell.Poison -> current.poison <= 1
                            Spell.Recharge -> current.recharge <= 1
                            else -> true
                        }
                    }
                }.map {
                    // simulate turn
                    current.playerTurn(it, constantDrain).bossTurn(damage)
                }.filter {
                    when {
                        // boss is dead, so check to see if the mana is less than current lowest. don't propagate state
                        it.bossHP <= 0 -> {
                            lowestManaWin = minOf(lowestManaWin, it.manaSpent)
                            false
                        }

                        // either player is dead, player can't cast a spell the next round, or the total mana spent
                        // is higher than the current lowest mana win. In all cases, player meets with failure so
                        // do not propagate
                        it.playerHP <= 0 || it.availableMana < 53 || it.manaSpent > lowestManaWin -> false
                        
                        // otherwise, not finished so propagate state
                        else -> true
                    }
                }
            states.addAll(nextStates)
        }
        return lowestManaWin
    }

    override fun part1() = solve(0)
    override fun part2() = solve(1)
}

fun main() = Day.runDay(Y2015D22::class)

//    Class creation: 2ms
//    Part 1: 1824 (222ms)
//    Part 2: 1937 (25ms)
//    Total time: 250ms