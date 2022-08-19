@file:Suppress("unused")

package org.gristle.adventOfCode.y2018.d15

import org.gristle.adventOfCode.utilities.*

/**
 * World functions
 */

fun Grid<Y2018D15.Entity>.finished() = count { it is Y2018D15.Elf }.let { elfCount -> elfCount == 0 || elfCount == count { it is Y2018D15.Player }
}

fun MutableGrid<Y2018D15.Entity>.kill(deadPlayer: Y2018D15.Player) {
    this[deadPlayer.pos] = Y2018D15.OpenSpace(deadPlayer.pos)
}

fun Grid<Y2018D15.Entity>.players() = filterIsInstance<Y2018D15.Player>()

fun Grid<Y2018D15.Entity>.print(round: Int, finishMidRound: Boolean = false) {
    val rep = buildString {
        val roundAnnouncement = when {
            round == 0 -> "Initially:"
            finishMidRound -> "During round $round:"
            round == 1 -> "After $round round:"
            else -> "After $round rounds:"
        }
        append("$roundAnnouncement\n")
        for (row in 0 until height) {
            val gloss = StringBuilder()
            for (col in 0 until width) {
                val c = when (val entity = get(col, row)) {
                    is Y2018D15.Wall -> '#'
                    is Y2018D15.Goblin -> {
                        gloss.append("G(${entity.health}), ")
                        'G'
                    }
                    is Y2018D15.Elf -> {
                        gloss.append("E(${entity.health}), ")
                        'E'
                    }
                    else -> '.'
                }
                append(c)
            }
            append("   ${gloss.trimEnd(',', ' ')}\n")
        }
    }

    println(rep)
}

class Y2018D15(val input: String) {

    /**
     * Classes and objects to define the game elements
     */

    sealed interface Entity {
        var pos: Coord
    }

    abstract class Player(override var pos: Coord, val damage: Int) : Entity {
        var health = 200
            private set

        private val isDead: Boolean get() = health <= 0

        fun playTurn(world: MutableGrid<Entity>) {
            // Quits if dead
            if (isDead) return

            // Get neighbors and see if they are a target
            val adjacentTargets = getAdjacentTargets(world)
            if (adjacentTargets.isEmpty()) {
                val targets = world.players().filter { isEnemy(it) }
                move(world, targets)
                val adjacentTargetsAfterMove = getAdjacentTargets(world)
                if (adjacentTargetsAfterMove.isNotEmpty()) {
                    attackAdjacent(adjacentTargetsAfterMove, world)
                }
            } else {
                attackAdjacent(adjacentTargets, world)
            }
        }

        private fun attack(target: Player) {
            target.damage(damage)
        }

        private fun attackAdjacent(targets: List<Player>, world: MutableGrid<Entity>) {
            val target = targets.minBy { it.health }
            attack(target)
            if (target.isDead) world.kill(target)
        }

        private fun damage(attackDamage: Int) {
            health -= attackDamage
        }

        fun move(world: MutableGrid<Entity>, targets: List<Player>) {

            val getEdges = { coord: Coord ->
                if (coord != pos && world[coord] is Player) {
                    emptyList()
                } else {
                    coord
                        .getNeighbors()
                        .filter { world.validCoord(it) && world[it] !is Wall }

                }
            }

            val distances = Graph.bfs(pos, defaultEdges = getEdges).drop(1)

            val spots = targets
                .flatMap { enemy -> world.getNeighborIndices(enemy.pos).map { world[it] }.filterIsInstance<OpenSpace>() }
                .distinct()
                .mapNotNull { openSpace -> distances.find { it.id == openSpace.pos } }

            val attackPos = spots
                .minByOrNull { it.weight * 10_000 + world.indexOf(it.id) } ?: return

            val newPos = Graph.bfs(attackPos.id, defaultEdges = getEdges)
                .filter { it.id.manhattanDistance(pos) == 1 && world[it.id] is OpenSpace }
                .sortedWith(compareBy<Graph.Vertex<Coord>> { it.weight }.thenBy { world.indexOf(it.id) })
                .first()
                .id

            val oldPos = pos
            world[newPos] = world[pos].apply { this.pos = newPos }
            world[oldPos] = OpenSpace(oldPos)
        }

        abstract fun getAdjacentTargets(world: MutableGrid<Entity>): List<Player>

        abstract fun isEnemy(other: Player): Boolean
    }

    class Wall(override var pos: Coord) : Entity {
        override fun toString() = "Wall(pos=$pos)"
    }

    class OpenSpace(override var pos: Coord) : Entity {
        override fun toString() = "OpenSpace(pos=$pos)"
    }

    class Elf(pos: Coord, damage: Int) : Player(pos, damage), Entity {
        override fun getAdjacentTargets(world: MutableGrid<Entity>) =
            world.getNeighbors(pos).filterIsInstance<Goblin>()

        override fun isEnemy(other: Player) = other !is Elf

        override fun toString() = "Elf(pos=$pos, damage=$damage, health=$health)"
    }

    class Goblin(pos: Coord) : Player(pos, 3), Entity {
        override fun getAdjacentTargets(world: MutableGrid<Entity>) =
            world.getNeighbors(pos).filterIsInstance<Elf>()

        override fun isEnemy(other: Player) = other !is Goblin

        override fun toString() = "Goblin(pos=$pos, damage=$damage, health=$health)"
    }

    /**
     * Main gameplay loop
     */
    fun solve(elfDamage: Int): Pair<String, Int> {

        // Initialize world
        val width = input.indexOfFirst { it in "\r\n" }
        val world = input.toGrid().mapToMutableGridIndexed { index, c ->
            val pos = Coord.fromIndex(index, width)
            when (c) {
                '#' -> Wall(pos)
                'E' -> Elf(pos, elfDamage)
                'G' -> Goblin(pos)
                else -> OpenSpace(pos)
            }
        }

        var round = 0

        val elves = world.players().count { it is Elf }

        fun p2Continue() = elfDamage == 3 || (elves == world.count { it is Elf })

        while (p2Continue() && world.filterIsInstance<Elf>().isNotEmpty() && world.filterIsInstance<Goblin>().isNotEmpty()) {

            val players = world.players()
            for (player in players.dropLast(1)) {
                player.playTurn(world)
            }
            if (world.finished()) round--
            if (players.isNotEmpty()) players.last().playTurn(world)
            round++
        }

        val remaining = world.players()
        val hp = remaining.sumOf { it.health }
        val victor = if (remaining.any { it is Goblin }) "Goblins" else "Elves"
        return victor to round * hp
    }

    fun part1() = solve(3).second

    fun part2() = generateSequence(4) { it + 1 }
        .map { solve(it) }
        .first { (victor, _) -> victor == "Elves" }
        .second
}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D15(readRawInput("y2018/d15"))
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 224370
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 45539 
}