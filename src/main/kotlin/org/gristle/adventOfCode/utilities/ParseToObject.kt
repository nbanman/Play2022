package org.gristle.adventOfCode.utilities

import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.typeOf

data class Dummy3(val int: Int, val string: String, val char: Char)

data class Dummy2(val int: Int, val char: Char)

data class Dummy(val name: String, val numbers: List<Int>)

data class CoordObj(val name: String, val pos: Coord, val pos2: Coord, val title: String)

fun main() {
    val t3 = listOf("3", "hi", "c")
    val t2 = listOf("3", "c")
    val t1 = listOf("Neil", "1, 2, 3")
    val dummy3 = parseObject<Dummy3>(::Dummy3, t3)
    val dummy2 = parseObject<Dummy2>(::Dummy2, t2)
    val dummy = parseObject<Dummy>(::Dummy, t1, Regex(", "))
    val otra = parseObject<CoordObj>(::CoordObj, listOf("Neil", "7", "5", "3", "2", "journeyman"))
    println("$otra, $dummy, $dummy2, $dummy3")
}

inline fun <reified E> parseObject(obj: KFunction<*>, arguments: List<String>, split: Regex? = null): E {
    val argIterator = arguments.iterator()
    val parameters = obj
        .parameters
        .map { parameter ->
            val argsNeeded = when (parameter.type) {
                typeOf<Coord>() -> 2
                else -> 1
            }
            parameter to (1..argsNeeded).map { argIterator.next() }
        }.associate { (parameter, argument) ->
            parameter to argument.convertTo(parameter.type, split)
        }
    val newObject = obj.callBy(parameters)
    if (newObject is E) return newObject else
        throw IllegalArgumentException("Cast failed: check that type parameter matches object reference")
}

fun List<String>.convertTo(type: KType, split: Regex?): Any {
    return when (type) {
        typeOf<Int>() -> first().toInt()
        typeOf<Long>() -> first().toLong()
        typeOf<Char>() -> first()[0]
        typeOf<String>() -> first()
        typeOf<Coord>() -> Coord(first().toInt(), last().toInt())
        typeOf<List<String>>() -> {
            if (split == null) throw IllegalArgumentException("Split Regex not defined for List")
            first().split(split)
        }

        typeOf<List<Int>>() -> {
            if (split == null) throw IllegalArgumentException("Split Regex not defined for List")
            first().split(split).map(String::toInt)
        }

        typeOf<List<Long>>() -> {
            if (split == null) throw IllegalArgumentException("Split Regex not defined for List")
            first().split(split).map(String::toLong)
        }

        typeOf<Set<String>>() -> {
            if (split == null) throw IllegalArgumentException("Split Regex not defined for List")
            first().split(split).toSet()
        }

        typeOf<Set<Int>>() -> {
            if (split == null) throw IllegalArgumentException("Split Regex not defined for List")
            first().split(split).map(String::toInt).toSet()
        }

        typeOf<Set<Long>>() -> {
            if (split == null) throw IllegalArgumentException("Split Regex not defined for List")
            first().split(split).map(String::toLong).toSet()
        }

        else -> throw IllegalArgumentException("Type not supported by converter.")
    }

}