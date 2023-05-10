package org.gristle.adventOfCode.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    (1..10).asFlow()
        .map { println("flow mapping $it ${Thread.currentThread()}"); it * 2 }
        .flowOn(Dispatchers.IO)
        .transform {
            println("transforming $it ${Thread.currentThread()}")
            emit(it * 2)
            emit(it * 3)
        }
        .first {
            println("Evaluating $it ${Thread.currentThread()}")
            it > 20
        }
        .let { println("flow result $it ${Thread.currentThread()}") }
}