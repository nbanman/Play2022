package org.gristle.adventOfCode.y2020.d4

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.blankSplit
import org.gristle.adventOfCode.utilities.groupValues

class Y2020D4(input: String) : Day {

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
            override fun isValid() = info.length == 9 && info.all(Char::isDigit)
        }

        object Cid : PassportField() {
            override fun isValid() = true
        }
    }

    private val passports = input
        .blankSplit()
        .map { rawPassportData ->
            rawPassportData
                .groupValues("""([a-z]{3}):([^ \r\n]+)""")
                .map(PassportField::fromGv)
                .filter { it !is PassportField.Cid }
        }.filter { passportFields -> passportFields.size == 7 }

    override fun part1() = passports.size

    override fun part2() = passports.count { it.all(PassportField::isValid) }
}

fun main() = Day.runDay(Y2020D4::class)

//    Class creation: 38ms
//    Part 1: 242 (0ms)
//    Part 2: 186 (8ms)
//    Total time: 47ms