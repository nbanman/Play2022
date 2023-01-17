package org.gristle.adventOfCode.utilities

import kotlin.reflect.KFunction3
import kotlin.reflect.KType
import kotlin.reflect.full.createType

data class Dummy(val int: Int, val string: String, val char: Char)

fun main() {
    val dummy = ::Dummy
    val t = listOf("3", "hi", "c")
    val parameters = dummy.parameters.zip(t).associate { it }
    println(parseObject3(dummy, t))
}

fun parseObject3(obj: KFunction3<*, *, *, *>, arguments: List<String>): Any? {

    val parameters = obj
        .parameters
        .zip(arguments)
        .associate { (parameter, argument) ->
            parameter to argument.convertTo(parameter.type)
        }
    return obj.callBy(parameters)
}

@OptIn(ExperimentalStdlibApi::class)
fun String.convertTo(type: KType): Any {
    return when (type) {
        Int::class.createType() -> toInt()
        Long::class.createType() -> toLong()
        Char::class.createType() -> this[0]
        String::class.createType() -> this
        else -> throw IllegalArgumentException("Type not supported by converter.")
    }
}