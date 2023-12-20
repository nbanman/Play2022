package org.gristle.adventOfCode.y2023.d20

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.lcm

class Y2023D20(input: String) : Day {
    
    enum class Pulse { HIGH, LOW }
    
    // Java OOP style, Modules are a bunch of objects with mutating state, strung together with various global 
    // queues and lookup tables.
    sealed interface Module {
        val name: String
        val downstream: List<String>
        
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
        override val downstream = listOf("broadcaster")
        
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
    
    class BroadCaster(override val downstream: List<String>) : Module {
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
    
    class FlipFlop(override val name: String, override val downstream: List<String>) : Module {
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
    
    class Conjunction(override val name: String, override val downstream: List<String>) : Module {
        init {
            Module.lookup[name] = this
            downstream.forEach { Module.upstreamCount[it] = (Module.upstreamCount[it] ?: 0) + 1 }
        }

        private val upstreamPulses = mutableMapOf<String, Pulse>()

        // Tracks the first time a conjunction box sends a high signal as part of a very basic cycle detection scheme
        // for pt 2.
        var firstHigh: Int? = null
        
        override fun onReceive(signal: Signal, round: Int) {
            upstreamPulses[signal.sender] = signal.pulse
            val pulse = if (Module.upstreamCount.getValue(name) == upstreamPulses.size &&
                upstreamPulses.values.all { it == Pulse.HIGH }) Pulse.LOW else Pulse.HIGH
            if (pulse == Pulse.HIGH) firstHigh = round // records the round of first high pulse
            val output = Signal(name, downstream, pulse)
            Module.dispatchQueue.add(output)
        }

        override fun reset() { 
            upstreamPulses.clear()
            firstHigh = null
        }
    }
    
    data class Signal(val sender: String, val recipients: List<String>, val pulse: Pulse) {
        fun send(round: Int) {
            recipients.forEach { name -> Module.lookup[name]?.onReceive(this, round) }
        }
    }

    init {
        // creates modules from input. No need to assign a variable because the objects are stored in a global lookup
        // table.
        input.lines().forEach { line ->
            val (nameStr, downstreamStr) = line.split(" -> ")
            val downstream = downstreamStr.split(", ").toList()
            when (nameStr[0]) {
                '%' -> FlipFlop(nameStr.drop(1), downstream)
                '&' -> Conjunction(nameStr.drop(1), downstream)
                else -> BroadCaster(downstream)
            }
        }
    }
    
    // Simulates one round by pushing the button, then processing signals until none are sent.
    // Returns the number of high and low pulses sent, which is used for Part 1. Part 2 relies on side effects.
    private fun pressButton(round: Int): Pair<Int, Int> {
        Button.onReceive(Signal(Button.name, listOf("broadcaster"), Pulse.LOW), 0)
        return generateSequence { Module.dispatchQueue.removeFirstOrNull() }
            .fold(0 to 0) { (high, low), signal ->
                signal.send(round)
                if (signal.pulse == Pulse.HIGH) {
                    high + signal.recipients.size to low
                } else {
                    high to low + signal.recipients.size
                }
            }
    }
    
    // Resets stateful objects, then returns a sequence that repeatedly presses the button and returns a sequence
    // of the high and low pulses sent that round.
    private fun rounds(): Sequence<Pair<Int, Int>> {
        Module.lookup.values.forEach { it.reset() }
        return generateSequence(1, Int::inc).map { pressButton(it) }
    }

    // Runs sequence 1000 times, sums up the high and low pulses sent, and multiplies the two together.
    override fun part1(): Int = rounds()
        .take(1000)
        .fold(0 to 0) { (sumHigh, sumLow), (high, low) ->
            sumHigh + high to sumLow + low
        }.let { (high, low) ->
            high * low
        }

    // Runs sequence until all conjunction modules have found their cycle, then returns the lcm of those conjunction
    // modules. As it turns out, the cycles have no offsets, every conjunction box that doesn't directly factor into
    // whether the box that sends a signal to 'rx' sends a high signal every round. So this simplified cycle detection
    // is sufficient.
    override fun part2(): Long {
        val conjunctions: List<Conjunction> = Module.lookup.values.filterIsInstance<Conjunction>()
        rounds().first { conjunctions.all { it.firstHigh != null } }
        return lcm(conjunctions.mapNotNull { it.firstHigh?.toLong() })
    }
}

fun main() = Day.runDay(Y2023D20::class)

//    Class creation: 9ms
//    Part 1: 938065580 (26ms)
//    Part 2: 250628960065793 (33ms)
//    Total time: 69ms

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