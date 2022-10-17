package org.gristle.adventOfCode.y2020.d4

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2020D4(input: String) {

    sealed class PassportField {
        abstract fun isValid(): Boolean

        companion object {
            fun fromGv(gv: List<String>): PassportField {
                return when (gv[0]) {
                    "byr" -> Byr(gv[1])
                    "iyr" -> Iyr(gv[1])
                    "eyr" -> Eyr(gv[1])
                    "hgt" -> Hgt(gv[1])
                    "hcl" -> Hcl(gv[1])
                    "ecl" -> Ecl(gv[1])
                    "pid" -> Pid(gv[1])
                    else -> Cid
                }
            }
        }

        class Byr(private val info: String) : PassportField() {
            override fun isValid() = (1920..2002).contains(info.toIntOrNull())
        }

        class Iyr(private val info: String) : PassportField() {
            override fun isValid() = (2010..2020).contains(info.toIntOrNull())
        }

        class Eyr(private val info: String) : PassportField() {
            override fun isValid() = (2020..2030).contains(info.toIntOrNull())
        }

        class Hgt(info: String) : PassportField() {
            private val amt = info.dropLast(2).toIntOrNull() ?: -1
            private val isCm = info.takeLast(2) == "cm"
            override fun isValid(): Boolean {
                return (isCm && amt in 150..193) || (!isCm && amt in 59..76)
            }
        }

        class Hcl(private val info: String) : PassportField() {
            override fun isValid() = """#[a-f\d]{6}""".toRegex().matches(info)
        }

        class Ecl(private val info: String) : PassportField() {
            override fun isValid() = """amb|blu|brn|gry|grn|hzl|oth""".toRegex().matches(info)
        }

        class Pid(private val info: String) : PassportField() {
            override fun isValid()= """\d{9}""".toRegex().matches(info)
        }

        object Cid : PassportField() {
            override fun isValid() = true
        }
    }

    private val passports = input
        .split("\r\n\r\n")
        .map { rawPassportData ->
            rawPassportData
                .groupValues("""([a-z]{3}):([^ \r\n]+)""")
                .map(PassportField::fromGv)
                .filter { it !is PassportField.Cid }
        }

    fun part1() = passports.count { it.size == 7 }

    fun part2() = passports.count { it.size == 7 && it.all(PassportField::isValid) }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D4(readRawInput("y2020/d4"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 242
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 186
}