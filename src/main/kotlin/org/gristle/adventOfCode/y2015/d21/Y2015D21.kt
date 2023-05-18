package org.gristle.adventOfCode.y2015.d21

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList
import org.gristle.adventOfCode.utilities.gvs

class Y2015D21(input: String) : Day {

    companion object {
        private const val WEAPONS = """Dagger        8     4       0
Shortsword   10     5       0
Warhammer    25     6       0
Longsword    40     7       0
Greataxe     74     8       0"""

        private const val ARMOR = """None          0     0       0
Leather      13     0       1
Chainmail    31     0       2
Splintmail   53     0       3
Bandedmail   75     0       4
Platemail   102     0       5"""

        private const val RINGS = """None          0     0       0
Damage +1    25     1       0
Damage +2    50     2       0
Damage +3   100     3       0
Defense +1   20     0       1
Defense +2   40     0       2
Defense +3   80     0       3"""
    }
    data class Boss(val hp: Int = 100, val damage: Int = 8, val armor: Int = 2) {
        fun roundsToDie(playerDamage: Int): Int {
            val adjustedDamage = maxOf(1, playerDamage - armor)
            return hp / adjustedDamage + if (hp % adjustedDamage == 0) 0 else 1
        }

        fun roundsToKill(playerArmor: Int, playerHp: Int): Int {
            val adjustedDamage = maxOf(1, damage - playerArmor)
            return playerHp / adjustedDamage + if (playerHp % adjustedDamage == 0) 0 else 1
        }
    }

    data class Item(val cost: Int, val damage: Int, val armor: Int)

    private val itemPattern = """\w+(?: \+\d)? +(\d+) +(\d) +(\d)""".toRegex()

    private fun getItemList(items: String): List<Item> = items
        .gvs(itemPattern, String::toInt)
        .map { (cost, damage, armor) -> Item(cost, damage, armor) }
        .toList()

    private val weaponList = getItemList(WEAPONS)
    private val armorList = getItemList(ARMOR)
    private val ringList = getItemList(RINGS)

    // Generate boss from input stats
    private val boss = input
        .getIntList()
        .let { (hp, damage, armor) -> Boss(hp, damage, armor) }

    // equip character
    private val combos = weaponList
        .fold(listOf<List<Item>>()) { acc, weapon ->
            acc + armorList.map { listOf(weapon, it) }
        }.fold(listOf<List<Item>>()) { acc, weaponArmor ->
            acc + ringList.map { weaponArmor + listOf(it) }
        }.fold(listOf<List<Item>>()) { acc, weaponArmorRing ->
            acc + ringList.map { weaponArmorRing + listOf(it) }
        }.distinct()

    private val combined = combos.map { combo ->
        Item(combo.sumOf { it.cost }, combo.sumOf { it.damage }, combo.sumOf { it.armor })
    }

    override fun part1() = combined
        .sortedBy(Item::cost)
        .first { boss.roundsToDie(it.damage) <= boss.roundsToKill(it.armor, 100) }
        .cost

    override fun part2() = combined
        .sortedByDescending(Item::cost)
        .first { boss.roundsToDie(it.damage) > boss.roundsToKill(it.armor, 100) }
        .cost
}

fun main() = Day.runDay(Y2015D21::class)

//    Class creation: 26ms
//    Part 1: 91 (3ms)
//    Part 2: 158 (2ms)
//    Total time: 33ms