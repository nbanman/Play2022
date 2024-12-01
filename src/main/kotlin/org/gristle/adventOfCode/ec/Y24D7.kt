package org.gristle.adventOfCode.ec

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.lcm
import org.gristle.adventOfCode.utilities.toGrid

fun main() {
    val (input1, input2, input3) = ecInputs(24, 7)
    val timer = Stopwatch(true)
    println("1. ${part1(input1)}: ${timer.lap()}ms")
    println("2. ${part2(input2)}: ${timer.lap()}ms")
    println("3. ${part3(input3)}: ${timer.lap()}ms")
    println("Total: ${timer.stop()}ms")
}

private fun part1(input: String): String {
    val knights = parseKnights(input)
    val race = track1
        .map { it.toPower() }
        .asSequence()
    return knights.map { (knight, plan) -> knight to getPower(plan, race) }
        .sortedByDescending { (_, power) -> power }
        .joinToString("") { (knight) -> knight}
}

private fun part2(input: String): String {
    val knights = parseKnights(input)
    val track: List<Int> = parseTrack(track2)
    val race: Sequence<Int> = getRace(track, 10)
    return knights.map { (knight, plan) -> knight to getPower(plan, race) }
        .sortedByDescending { (_, power) -> power }
        .joinToString("") { (knight) -> knight}
}

private fun part3(input: String): Int {
    val (_, opponentPlan) = parseKnights(input).first()
    val track = parseTrack(track3)
    val lcm = lcm(11, track.size.toLong()).toInt()
    val adjustedLoops = lcm / track.size
    val race: Sequence<Int> = getRace(track, adjustedLoops)
    val opponentPower = getPower(opponentPlan, race)
    val plans = getPlans()

    return plans.count { plan -> getPower(plan, race) > opponentPower }
}

private fun parseKnights(input: String): List<Pair<String, Sequence<Int>>> = input.lines().map { line ->
    val (label, powerStr) = line.split(':')
    val power: List<Int> = powerStr.split(',').map { c -> c[0].toPower() }
    label to generateSequence(0, Int::inc).map { power[it % power.size] }
}

private fun getPower(plan: Sequence<Int>, race: Sequence<Int>): Long {
    val powerSeq: Sequence<Pair<Int, Int>> = race zip plan
    return powerSeq
        .runningFold(10L) { acc, (track, device) ->
            val adjust = if (track == 0) device else track
            val newAcc = (acc + adjust).coerceAtLeast(0)
            newAcc
        }.drop(1)
        .sum()
}

private fun parseTrack(rawLoop: String): List<Int> = buildList {
    val width = rawLoop.indexOf('\n')
    val paddedLoop = rawLoop.lines().joinToString("\n") { line -> line.padEnd(width) }
    val loop = paddedLoop.toGrid()
    val turns = listOf(Nsew::straight, Nsew::left, Nsew::right).asSequence()
    val move: (Pair<Coord, Nsew>) -> Pair<Coord, Nsew> = { (pos, dir) ->
        turns
            .map { turn ->
                val newDir = turn(dir)
                val newPos = pos.move(newDir)
                newPos to newDir
            }.first { (pos) -> loop.validCoord(pos) && loop[pos] != ' ' }
    }
    generateSequence(Coord(1, 0) to Nsew.EAST, move)
        .first { (pos) ->
            val action = loop[pos]
            add(action.toPower())
            action == 'S'
        }
}

private fun Char.toPower() = when (this) {
    '+' -> 1
    '-' -> -1
    else -> 0
}

private fun getRace(loop: List<Int>, adjustedLoops: Int): Sequence<Int> = generateSequence(loop) { loop }
    .take(adjustedLoops)
    .flatMap { it }

private fun getPlans(): List<Sequence<Int>> {
    val permutations = ArrayList<List<Int>>(9240)
    val working = ArrayList<Int>(11)
    val store = intArrayOf(3, 3, 5)

    fun traverse() {
        for (value in 0..2) {
            if (store[value] > 0) {
                working.add(value - 1)
                store[value]--
                if (working.size == 11) {
                    permutations.add(working.toList())
                } else {
                    traverse()
                }
                working.removeLast()
                store[value]++
            }
        }
    }
    traverse()
    return permutations.map { permutation ->
        generateSequence(0, Int::inc).map { permutation[it % permutation.size] }
    }
}

private const val track1 = "=========="

private val track2 = """
    S-=++=-==++=++=-=+=-=+=+=--=-=++=-==++=-+=-=+=-=+=+=++=-+==++=++=-=-=--
    -                                                                     -
    =                                                                     =
    +                                                                     +
    =                                                                     +
    +                                                                     =
    =                                                                     =
    -                                                                     -
    --==++++==+=+++-=+=-=+=-+-=+-=+-=+=-=+=--=+++=++=+++==++==--=+=++==+++-
""".trimIndent()

private val track3 = """
    S+= +=-== +=++=     =+=+=--=    =-= ++=     +=-  =+=++=-+==+ =++=-=-=--
    - + +   + =   =     =      =   == = - -     - =  =         =-=        -
    = + + +-- =-= ==-==-= --++ +  == == = +     - =  =    ==++=    =++=-=++
    + + + =     +         =  + + == == ++ =     = =  ==   =   = =++=
    = = + + +== +==     =++ == =+=  =  +  +==-=++ =   =++ --= + =
    + ==- = + =   = =+= =   =       ++--          +     =   = = =--= ==++==
    =     ==- ==+-- = = = ++= +=--      ==+ ==--= +--+=-= ==- ==   =+=    =
    -               = = = =   +  +  ==+ = = +   =        ++    =          -
    -               = + + =   +  -  = + = = +   =        +     =          -
    --==++++==+=+++-= =-= =-+-=  =+-= =-= =--   +=++=+++==     -=+=++==+++-
""".trimIndent()