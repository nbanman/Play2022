package org.gristle.adventOfCode.utilities

import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.typeOf

data class Dummy3(val int: Int, val string: String, val char: Char)

data class Dummy2(val int: Int, val char: Char)

data class Dummy(val name: String, val numbers: List<Int>)

fun main() {
    val t3 = listOf("3", "hi", "c")
    val t2 = listOf("3", "c")
    val t1 = listOf("Neil", "1, 2, 3")
    val dummy3 = parseObject<Dummy3>(::Dummy3, t3)
    val dummy2 = parseObject<Dummy2>(::Dummy2, t2)
    val dummy = parseObject<Dummy>(::Dummy, t1, Regex(", "))
    println("$dummy, $dummy2, $dummy3")
}

@Suppress("UNCHECKED_CAST")
fun <E> parseObject(obj: KFunction<*>, arguments: List<String>, split: Regex? = null): E {
    val parameters = obj
        .parameters
        .zip(arguments)
        .associate { (parameter, argument) ->
            parameter to argument.convertTo(parameter.type, split)
        }
    return obj.callBy(parameters) as E
}

fun String.convertTo(type: KType, split: Regex?): Any {
    return when (type) {
        typeOf<Int>() -> toInt()
        typeOf<Long>() -> toLong()
        typeOf<Char>() -> this[0]
        typeOf<String>() -> this
        typeOf<List<String>>() -> {
            if (split == null) throw IllegalArgumentException("Split Regex not defined for List")
            this.split(split)
        }

        typeOf<List<Int>>() -> {
            if (split == null) throw IllegalArgumentException("Split Regex not defined for List")
            this.split(split).map(String::toInt)
        }

        typeOf<List<Long>>() -> {
            if (split == null) throw IllegalArgumentException("Split Regex not defined for List")
            this.split(split).map(String::toLong)
        }

        else -> throw IllegalArgumentException("Type not supported by converter.")
    }
}