package org.gristle.adventOfCode.utilities

class Stopwatch(start: Boolean = false) {
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
        return elapsed / 1_000_000
    }

    fun lap(): Long =
        if (isRunning) {
            val now = System.nanoTime()
            val lap = now - lastStart
            elapsed += lap
            lastStart = now
            lap / 1_000_000
        } else 0


    fun reset() {
        elapsed = 0
        lastStart = 0
        isRunning = false
    }

    fun elapsed() = elapsed / 1_000_000

}