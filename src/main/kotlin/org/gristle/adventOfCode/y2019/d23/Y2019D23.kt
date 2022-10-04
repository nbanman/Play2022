package org.gristle.adventOfCode.y2019.d23

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.y2019.IntCode.IntCode
import java.util.*

class Y2019D23(private val input: String) {

    data class Nat(val x: Long, val y: Long)

    fun solve(): Pair<Long, Long> {
        val initialState = input.split(',').map { it.toLong() }
        val inputs = List(50) { LinkedList<Long>() }
        val outputs = List(50) { LinkedList<Long>() }
        val nics = List(50) { i ->
            IntCode("$i", initialState, null, inputs[i], outputs[i]).apply {
                inputs[i].add(i.toLong())
                run()
            }
        }
        val nat = mutableListOf<Nat>()
        var lastYSent = -1L
        while (true) {
            inputs.forEach { if (it.isEmpty()) it.add(-1L) }
            nics.forEach { nic ->
                val id = nic.name.toInt()
                nic.run()
                for ((recipient, x, y) in outputs[id].chunked(3)) {
                    val recId = recipient.toInt()
                    if (recId == 255) {
                        nat.add(Nat(x, y))
                    } else {
                        inputs[recId].add(x)
                        inputs[recId].add(y)
                    }
                }
                outputs[id].clear()
            }
            if (inputs.all { it.isEmpty() }) {
                if (nat.last().y == lastYSent) break
                inputs[0].add(nat.last().x)
                inputs[0].add(nat.last().y)
                lastYSent = nat.last().y
            }
        }
        return nat.first().y to lastYSent
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2019D23(readRawInput("y2019/d23"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    val (p1, p2) = c.solve()
    println("Part 1: $p1") // 23701
    println("Part 2: $p2 (${elapsedTime(time)}ms)") // 17225
}