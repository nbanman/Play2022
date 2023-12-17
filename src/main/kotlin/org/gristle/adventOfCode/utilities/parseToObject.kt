@file:Suppress("unused")

package org.gristle.adventOfCode.utilities

import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.typeOf

data class Dummy3(val int: Int, val string: String, val char: Char)

data class Dummy2(val int: Int, val char: Char) {
    companion object {
        fun of(char: Char, int: Int) = Dummy2(int, char)
    }
}

data class Dummy(val name: String, val numbers: List<Int>)

data class CoordObj(val name: String, val pos: Coord, val pos2: Coord, val title: String)

fun makeDummy2(char: Char, int: Int) = Dummy2(int, char)

fun main() {
    val t3 = listOf("3", "hi", "c")
    val t2 = listOf("3", "c")
    val t1 = listOf("Neil", "1, 2, 3")
    val dummy3 = t3.parseToObject(::Dummy3)
    val dummy2 = t2.parseToObject(::Dummy2)
    val dummy2b = listOf("c", "3").parseToObject(Dummy2::of)
    val dummy = t1.parseToObject(::Dummy, Regex(", "))
    val other = listOf("Neil", "7", "5", "3", "2", "journeyman").parseToObject(::CoordObj)


    println("$dummy2b")
}

fun <E : Any> List<String>.parseToObject(kFun: KFunction<E>, split: Regex? = null): E {
    val arguments = iterator()
    val parameters = kFun.parameters.associateWith { arguments.convertTo(it, split) }
    return kFun.callBy(parameters)
}

fun <E : Any> List<List<String>>.parseToObject(kFun: KFunction<E>, split: Regex? = null): List<E> {
    return map { line -> line.parseToObject(kFun, split) }
}

fun Iterator<String>.convertTo(parameter: KParameter, split: Regex?): Any {

    return when (parameter.type) {
        typeOf<Int>() -> next().toInt()
        typeOf<Long>() -> next().toLong()
        typeOf<Char>() -> next()[0]
        typeOf<String>() -> next()
        typeOf<Coord>() -> Coord(next().toInt(), next().toInt())
        typeOf<Xyz>() -> Xyz(next().toInt(), next().toInt(), next().toInt())
        typeOf<List<String>>() -> {
            if (split == null) throw IllegalArgumentException("Split Regex not defined for List")
            next().split(split)
        }

        typeOf<List<Int>>() -> {
            if (split == null) throw IllegalArgumentException("Split Regex not defined for List")
            next().split(split).map(String::toInt)
        }

        typeOf<List<Long>>() -> {
            if (split == null) throw IllegalArgumentException("Split Regex not defined for List")
            next().split(split).map(String::toLong)
        }

        typeOf<Set<String>>() -> {
            if (split == null) throw IllegalArgumentException("Split Regex not defined for List")
            next().split(split).toSet()
        }

        typeOf<Set<Int>>() -> {
            if (split == null) throw IllegalArgumentException("Split Regex not defined for List")
            next().split(split).map(String::toInt).toSet()
        }

        typeOf<Set<Long>>() -> {
            if (split == null) throw IllegalArgumentException("Split Regex not defined for List")
            next().split(split).map(String::toLong).toSet()
        }

        else -> {

            throw IllegalArgumentException("Type not supported by converter.")
        }
    }
}