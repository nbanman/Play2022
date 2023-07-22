package org.gristle.adventOfCode.y2018.d15

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Graph
import java.util.*

class Y2018D15(val input: String) : Day {

    /**
     * Abstract class for both elves and goblins. Responsible for tracking health, playing each players turn,
     * including attacking and moving
     */
    abstract class Player {
        var health = 200

        private val isDead: Boolean get() = health < 1

        /**
         * Finds the player at the specified other position and commands it to take damage.
         */
        private fun attack(world: World, otherPos: Coord) {
            world[otherPos]?.takeDamage(world, otherPos, damage(world))
        }

        /**
         * Looks for adjacent targets. If found, attack the one with lowest health. Otherwise, move to closest
         * target and attack if now adjacent.
         */
        fun playTurn(world: World, pos: Coord) {
            if (isDead) return

            val enemies = enemies(world)

            // utility function for finding adjacent targets to attack
            fun adjacentOpponentPos(pos: Coord) = pos
                .getNeighbors()
                .filter { it in enemies }
                .minByOrNull { enemies.getValue(it).health }

            // find adjacent target...
            val adjacentTarget = adjacentOpponentPos(pos)

            if (adjacentTarget != null) {
                attack(world, adjacentTarget) // ...and attack it
            } else { // if no adjacent target...
                val newPos = move(world, pos) // move one space to a target
                val adjacentTargetAfterMove = adjacentOpponentPos(newPos) // and attack if adjacent
                if (adjacentTargetAfterMove != null) attack(world, adjacentTargetAfterMove)
            }
        }

        /**
         * Takes damage and removes the player from the world if dead.
         */
        private fun takeDamage(world: World, pos: Coord, damage: Int) {
            health -= damage
            if (isDead) {
                friends(world).remove(pos)
            }
        }

        /**
         * Finds closest spot adjacent to a target and move one step toward it. Uses BFS
         */
        fun move(world: World, pos: Coord): Coord {
            val friends = friends(world)
            val enemies = enemies(world)

            // The coordinates that are adjacent to targets.
            val inRange: Set<Coord> = enemies
                .keys
                .flatMap { opponentPos -> opponentPos.getNeighbors().filter { world.canMove(it) } }
                .toSet()

            // BFS queue
            val q: Deque<Graph.Vertex<Coord>> = ArrayDeque()
            val start = Graph.StdVertex(pos, 0.0)
            q.addLast(start)
            val visited = mutableMapOf<Coord, Graph.StdVertex<Coord>>()

            // BFS loop
            while (q.isNotEmpty()) {
                val current = q.removeFirst()

                // for each vertex find neighbor vertices
                val neighbors = current
                    .id
                    .getNeighbors()
                    .filter { neighbor -> world.canMove(neighbor) }
                    .map { Graph.StdVertex(it, current.weight + 1, current) }

                for (neighbor in neighbors) {
                    if (neighbor.id !in visited) {
                        q.add(neighbor)
                        visited[neighbor.id] = neighbor

                        if (neighbor.id in inRange) {
                            val steps = visited.values.last().weight
                            val chosen = visited
                                .values
                                .filter { it.weight == steps && it.id in inRange }
                                .minBy { it.id.asIndex(world.width) }
                            val newPos = chosen
                                .path()[1]
                                .id
                            friends.remove(pos)
                            friends[newPos] = this
                            return newPos
                        }
                    }
                }
            }
            return pos
        }

        abstract fun damage(world: World): Int

        abstract fun friends(world: World): MutableMap<Coord, Player>

        abstract fun enemies(world: World): MutableMap<Coord, Player>

    }

    class Elf : Player() {
        override fun friends(world: World) = world.elves
        override fun enemies(world: World) = world.goblins
        override fun damage(world: World) = world.elfDamage
    }

    class Goblin : Player() {
        override fun friends(world: World) = world.goblins
        override fun enemies(world: World) = world.elves
        override fun damage(world: World) = 3
    }

    data class World(
        val width: Int,
        val height: Int,
        private val walls: Set<Coord>,
        val elves: MutableMap<Coord, Player>,
        val goblins: MutableMap<Coord, Player>,
        val elfDamage: Int
    ) {
        private val initialElves = elves.size

        fun clone(elfDamage: Int): World {
            val newElves: MutableMap<Coord, Player> = elves
                .entries
                .associateTo(mutableMapOf()) { (pos) -> pos to Elf() }
            val newGoblins: MutableMap<Coord, Player> = goblins
                .entries
                .associateTo(mutableMapOf()) { (pos) -> pos to Goblin() }

            return World(width, height, walls, newElves, newGoblins, elfDamage)
        }

        fun canMove(pos: Coord): Boolean = pos.x in 0 until width
                && pos.y in 0 until height
                && pos !in walls
                && pos !in elves
                && pos !in goblins

        fun elfHealth() = elves.values.sumOf { it.health }
        fun goblinHealth() = goblins.values.sumOf { it.health }

        fun players() = (elves.entries + goblins.entries).sortedBy { (pos) -> pos.asIndex(width) }

        fun elvesLose(): Boolean = elves.isEmpty() || (elfDamage > 3 && elves.size < initialElves)

        operator fun get(pos: Coord): Player? = elves[pos] ?: goblins[pos]

        companion object {
            fun from(input: String): World {
                val width = input.indexOf('\n')
                val noSpace = input.replace("\n", "")
                val height = noSpace.length / width
                val elves = mutableMapOf<Coord, Player>()
                val goblins = mutableMapOf<Coord, Player>()
                val walls = mutableSetOf<Coord>()
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val pos = Coord(x, y)
                        when (noSpace[y * width + x]) {
                            '#' -> walls.add(pos)
                            'E' -> elves[pos] = Elf()
                            'G' -> goblins[pos] = Goblin()
                        }
                    }
                }
                return World(width, height, walls, elves, goblins, 3)
            }
        }
    }

    private val initialWorld = World.from(input)

    data class Game(val round: Round, val opponentHp: Int) {
        fun score(): Int = round.round * opponentHp
    }

    enum class WinState { ELVES, GOBLINS, CONTINUE }

    data class Round(val round: Int = 0, val winState: WinState = WinState.CONTINUE)

    fun solve(elfDamage: Int = 3): Game {
        val world = initialWorld.clone(elfDamage)


        fun playRound(round: Round): Round {
            val players = world.players()
            players.forEachIndexed { index, (pos, player) ->
                player.playTurn(world, pos)

                val winState = when {
                    world.elvesLose() -> WinState.GOBLINS
                    world.goblins.isEmpty() -> WinState.ELVES
                    else -> WinState.CONTINUE
                }

                if (winState != WinState.CONTINUE) {
                    val winRound = if (index == players.lastIndex) round.round + 1 else round.round
                    return Round(winRound, winState)
                }
            }
            return round.copy(round = round.round + 1)
        }

        generateSequence(Round(), ::playRound)
            .first { (_, winState) -> winState != WinState.CONTINUE }
            .let { round ->
                val opponentHp = if (round.winState == WinState.ELVES) world.elfHealth() else world.goblinHealth()
                return Game(round, opponentHp)
            }
    }

    override fun part1() = solve().score()

    override fun part2() = generateSequence(4) { elfDamage -> elfDamage + 1 }
        .map { elfDamage -> solve(elfDamage) }
        .first { game -> game.round.winState == WinState.ELVES }
        .score()
}

fun main() = Day.runDay(Y2018D15::class)

//    Class creation: 5ms
//    Part 1: 224370 (140ms)
//    Part 2: 45539 (252ms)
//    Total time: 399ms