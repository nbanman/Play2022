package org.gristle.adventOfCode.y2019.d4

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2019D4(input: String) {

    // passwords for part 1 are calculated in advance because used for both parts
    private val part1Passwords = input // take input
        .split('-') // split String into two Strings representing numbers
        .map(String::toInt) // convert the Strings to Int for easy range creation
        .let { it[0]..it[1] } // create range between the two numbers
        .map(Int::toString) // convert each number in range to String for rule analysis
        .filter { password -> // apply part 1 rules b/c they apply to both parts
            val zippedPassString = password.zipWithNext() // compare each digit with the one after it
            zippedPassString.none { it.first > it.second } // digits never decrease 
                    && zippedPassString.any { it.first == it.second } // at least one is the same
        }

    fun part1() = part1Passwords.size // return number of passwords

    fun part2(): Int {
        val pattern = Regex("""(\d)\1+""") // Regex looks for repeated digits of any length
        return part1Passwords
            // For each password, find all sequences of repeated digits and keep those that have at least one
            // sequence that only has two digits.
            .filter { password -> pattern.findAll(password).any { it.value.length == 2 } }
            .size // return number of passwords matching this criteria
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2019D4(readRawInput("y2019/d4"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 466
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 292
}