package org.gristle.adventOfCode.utilities

enum class TimeUnits(val divisor: Long) {
    S(1_000_000_000),
    MS(1_000_000),
    US(1_000),
    NS(1)
}

class Stopwatch(start: Boolean = false, val units: TimeUnits = TimeUnits.MS) {
    private var elapsed = 0L
    private var lastStart = 0L
    private var isRunning = false

    init {
        if (start) start()
    }

    fun start(): Boolean =
        if (isRunning) false else {
            lastStart = System.nanoTime()
            isRunning = true
            true
        }

    fun stop(): Long {
        val now = System.nanoTime()
        if (isRunning) {
            isRunning = false
            elapsed += now - lastStart
        }
        return elapsed / units.divisor
    }

    fun lap(): Long =
        if (isRunning) {
            val now = System.nanoTime()
            val lap = now - lastStart
            elapsed += lap
            lastStart = now
            lap / units.divisor
        } else 0


    fun reset() {
        elapsed = 0
        lastStart = 0
        isRunning = false
    }

    fun elapsed() = elapsed / units.divisor

}