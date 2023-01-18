package org.gristle.adventOfCode.utilities

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.typeOf

data class Dummy3(val int: Int, val string: String, val char: Char)

data class Dummy2(val int: Int, val char: Char)

data class Dummy(val name: String, val numbers: List<Int>)

data class CoordObj(val name: String, val pos: Coord, val pos2: Coord, val title: String)

fun main() {
    val t3 = listOf("3", "hi", "c")
    val t2 = listOf("3", "c")
    val t1 = listOf("Neil", "1, 2, 3")
    val dummy3 = t3.parseToObject(Dummy3::class)
    val dummy2 = t2.parseToObject(Dummy2::class)
    val dummy = t1.parseToObject(Dummy::class, Regex(", "))
    val other = listOf("Neil", "7", "5", "3", "2", "journeyman").parseToObject(CoordObj::class)
    println("$other, $dummy, $dummy2, $dummy3")
}

fun <E : Any> List<List<String>>.parseToObjects(kClass: KClass<E>, split: Regex? = null): List<E> {
    return map { line -> line.parseToObject(kClass, split) }
}

fun <E : Any> Sequence<List<String>>.parseToObjects(
    kClass: KClass<E>,
    split: Regex? = null
): Sequence<E> {
    return map { line -> line.parseToObject(kClass, split) }
}

fun <E : Any> List<String>.parseToObject(kClass: KClass<E>, split: Regex? = null): E {
    val constructor = kClass.constructors.first()
    val arguments = iterator()
    val parameters = constructor
        .parameters
        .associateWith { arguments.convertTo(it, split) }
    return constructor.callBy(parameters)
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

        else -> throw IllegalArgumentException("Type not supported by converter.")
    }
}