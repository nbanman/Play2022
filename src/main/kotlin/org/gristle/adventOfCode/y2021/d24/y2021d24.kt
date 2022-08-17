package org.gristle.adventOfCode.y2021.d24

import org.gristle.adventOfCode.utilities.*
import java.util.LinkedList
import kotlin.math.max
import kotlin.math.min

object Y2021D24 {
    private val input = readInput("y2021/d24")
    
    data class Step(val order: Int, val x: Int, val y: Int)
    data class PairedSteps(val push: Step, val pop: Step)
    
    val steps = input
        .chunked(18)
        .mapIndexed { index, lines ->
            val xInc = lines[5].substringAfterLast(" ").toInt()
            val yInc = lines[15].substringAfterLast(" ").toInt()
            Step(index, xInc, yInc)
        }.let { steps -> 
            val pairedSteps= mutableListOf<PairedSteps>()
            val orderStack = LinkedList<Step>()
            steps.forEach { step ->
                if (step.x >= 0) {
                    orderStack.push(step)
                } else {
                    pairedSteps.add(PairedSteps(orderStack.pop(), step))
                }
            }
            pairedSteps
        }.sortedBy { it.push.order }

    fun solve(forMin: Boolean): String {
        var z = 0L
        var lastIncrease = 0
        val modelNumber = MutableList(steps.size * 2) { 0 }
        steps.forEach { step ->
            val increaseZ = step.push.y + 26 * z
            val pushMax = ((increaseZ + 9) % 26).toInt() 
            val popMax = -step.pop.x + 9 
            val intersection = if (forMin) {
                max(pushMax, popMax) - 8
            } else {
                min(pushMax, popMax)
            }
            modelNumber[step.push.order] = 9 - (pushMax - intersection)
            modelNumber[step.pop.order] = 9 - (popMax - intersection)
            repeat(step.push.order - lastIncrease - 1) { z /= 26 }
            z = increaseZ + modelNumber[step.push.order]
            lastIncrease = step.push.order
        }
        
        return modelNumber.joinToString("")
    }
    fun part1() = solve(false)

    fun part2() = solve(true)
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D24.part1()} (${elapsedTime(time)}ms)") // 92969593497992
    time = System.nanoTime()
    println("Part 2: ${Y2021D24.part2()} (${elapsedTime(time)}ms)") // 81514171161381
}