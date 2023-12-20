package org.gristle.adventOfCode.y2023.d20

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.lcm

class Y2023D20(input: String) : Day {
    
    enum class Pulse { HIGH, LOW }
    
    sealed interface Module {
        val name: String
        val downstream: Set<String>
        
        fun onReceive(signal: Signal, round: Int)
        
        fun reset()
        
        companion object {
            internal val dispatchQueue = ArrayDeque<Signal>()
            internal val lookup = mutableMapOf<String, Module>()
            internal val upstreamCount = mutableMapOf<String, Int>()
        }
    }
    
    data object Button : Module {
        override val name = "button"
        override val downstream = setOf("broadcaster")
        
        init {
            Module.lookup[name] = this
            downstream.forEach { Module.upstreamCount[it] = (Module.upstreamCount[it] ?: 0) + 1 }
        }

        override fun onReceive(signal: Signal, round: Int) {
            val output = Signal(name, downstream, Pulse.LOW)
            Module.dispatchQueue.add(output)
        }

        override fun reset() { }
    }
    
    class BroadCaster(override val downstream: Set<String>) : Module {
        override val name = "broadcaster"
        
        init {
            Module.lookup[name] = this
            downstream.forEach { Module.upstreamCount[it] = (Module.upstreamCount[it] ?: 0) + 1 }
        }

        override fun onReceive(signal: Signal, round: Int) {
            val output = Signal(name, downstream, signal.pulse)
            Module.dispatchQueue.add(output)
        }

        override fun reset() { }
    }
    
    class FlipFlop(override val name: String, override val downstream: Set<String>) : Module {
        init {
            Module.lookup[name] = this
            downstream.forEach { Module.upstreamCount[it] = (Module.upstreamCount[it] ?: 0) + 1 }
        }

        private var on = false

        override fun onReceive(signal: Signal, round: Int) {
            if (signal.pulse == Pulse.LOW) {
                on = !on
                val pulse = if (on) Pulse.HIGH else Pulse.LOW
                val output = Signal(name, downstream, pulse)
                Module.dispatchQueue.add(output)
            }
        }

        override fun reset() { 
            on = false
        }
    }
    
    class Conjunction(override val name: String, override val downstream: Set<String>) : Module {
        init {
            Module.lookup[name] = this
            downstream.forEach { Module.upstreamCount[it] = (Module.upstreamCount[it] ?: 0) + 1 }
        }

        private val upstreamPulses = mutableMapOf<String, Pulse>()

        var firstHigh: Int? = null
        
        override fun onReceive(signal: Signal, round: Int) {
            upstreamPulses[signal.sender] = signal.pulse
            val pulse = if (Module.upstreamCount.getValue(name) == upstreamPulses.size &&
                upstreamPulses.values.all { it == Pulse.HIGH }) Pulse.LOW else Pulse.HIGH
            if (pulse == Pulse.HIGH) firstHigh = round
            val output = Signal(name, downstream, pulse)
            Module.dispatchQueue.add(output)
        }

        override fun reset() { 
            upstreamPulses.clear()
            firstHigh = null
        }
    }
    
    data class Signal(val sender: String, val recipients: Set<String>, val pulse: Pulse) {
        fun send(round: Int) {
            recipients.forEach { name -> Module.lookup[name]?.onReceive(this, round) }
        }
    }

    init {
        input.lines().forEach { line ->
            val (nameStr, downstreamStr) = line.split(" -> ")
            val downstream = downstreamStr.split(", ").toSet()
            when (nameStr[0]) {
                '%' -> FlipFlop(nameStr.drop(1), downstream)
                '&' -> Conjunction(nameStr.drop(1), downstream)
                else -> BroadCaster(downstream)
            }
        }
    }
    
    private fun pressButton(): Pair<Int, Int> {
        Button.onReceive(Signal(Button.name, setOf("broadcaster"), Pulse.LOW), 0)
        return generateSequence { Module.dispatchQueue.removeFirstOrNull() }
            .fold(0 to 0) { (high, low), signal ->
                signal.send(0)
                if (signal.pulse == Pulse.HIGH) {
                    high + signal.recipients.size to low
                } else {
                    high to low + signal.recipients.size
                }
            }
    }
    
    private fun rxButton(round: Int): Boolean {
        Button.onReceive(Signal(Button.name, setOf("broadcaster"), Pulse.LOW), 1)
        return generateSequence { Module.dispatchQueue.removeFirstOrNull() }
            .any { signal -> 
                if ("rx" in signal.recipients && signal.pulse == Pulse.LOW) {
                    true
                } else {
                    signal.send(round)
                    false
                }
            }
    }

    override fun part1() = generateSequence { pressButton() }
        .take(1000)
        .fold(0L to 0L) { (sumHigh, sumLow), (high, low) ->
            sumHigh + high to sumLow + low
        }.let { (high, low) -> 
            high * low 
        }

    override fun part2(): Long {
        Module.lookup.values.forEach { it.reset() }
        val conjunctions: List<Conjunction> = Module.lookup.values.filterIsInstance<Conjunction>()
        generateSequence(1, Int::inc)
            .map { rxButton(it) }
            .first { conjunctions.all { it.firstHigh != null } }
        return lcm(conjunctions.mapNotNull { it.firstHigh?.toLong() })
    }
}

fun main() = Day.runDay(Y2023D20::class)

//    Class creation: 9ms
//    Part 1: 938065580 (25ms)
//    Part 2: 250628960065793 (47ms)
//    Total time: 82ms

@Suppress("unused")
private val sampleInput = listOf(
    """broadcaster -> a, b, c
%a -> b
%b -> c
%c -> inv
&inv -> a
""", """broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> output
""", 
)