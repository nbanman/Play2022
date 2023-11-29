package org.gristle.adventOfCode.utilities

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.y2018.d1.Y2018D1
import org.gristle.adventOfCode.y2018.d10.Y2018D10
import org.gristle.adventOfCode.y2018.d11.Y2018D11
import org.gristle.adventOfCode.y2018.d12.Y2018D12
import org.gristle.adventOfCode.y2018.d13.Y2018D13
import org.gristle.adventOfCode.y2018.d14.Y2018D14
import org.gristle.adventOfCode.y2018.d15.Y2018D15
import org.gristle.adventOfCode.y2018.d16.Y2018D16
import org.gristle.adventOfCode.y2018.d17.Y2018D17
import org.gristle.adventOfCode.y2018.d18.Y2018D18
import org.gristle.adventOfCode.y2018.d19.Y2018D19
import org.gristle.adventOfCode.y2018.d2.Y2018D2
import org.gristle.adventOfCode.y2018.d21.Y2018D21
import org.gristle.adventOfCode.y2018.d22.Y2018D22
import org.gristle.adventOfCode.y2018.d23.Y2018D23
import org.gristle.adventOfCode.y2018.d24.Y2018D24
import org.gristle.adventOfCode.y2018.d25.Y2018D25
import org.gristle.adventOfCode.y2018.d3.Y2018D3
import org.gristle.adventOfCode.y2018.d4.Y2018D4
import org.gristle.adventOfCode.y2018.d5.Y2018D5
import org.gristle.adventOfCode.y2018.d6.Y2018D6
import org.gristle.adventOfCode.y2018.d7.Y2018D7
import org.gristle.adventOfCode.y2018.d8.Y2018D8
import org.gristle.adventOfCode.y2018.d9.Y2018D9
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Test2018 {
    @Test
    internal fun y2018d1() {
        assertEquals(433 to 256, Day.testDay(Y2018D1::class))
    }

    @Test
    internal fun y2018d2() {
        assertEquals(7688 to "lsrivmotzbdxpkxnaqmuwcchj", Day.testDay(Y2018D2::class))
    }

    @Test
    internal fun y2018d3() {
        assertEquals(110891 to 297, Day.testDay(Y2018D3::class))
    }

    @Test
    internal fun y2018d4() {
        assertEquals(19025 to 23776, Day.testDay(Y2018D4::class))
    }

    @Test
    internal fun y2018d5() {
        assertEquals(10972 to 5278, Day.testDay(Y2018D5::class))
    }

    @Test
    internal fun y2018d6() {
        assertEquals(5365 to 42513, Day.testDay(Y2018D6::class))
    }

    @Test
    internal fun y2018d7() {
        assertEquals("ABGKCMVWYDEHFOPQUILSTNZRJX" to 898, Day.testDay(Y2018D7::class))
    }

    @Test
    internal fun y2018d8() {
        assertEquals(36027 to 23960, Day.testDay(Y2018D8::class))
    }

    @Test
    internal fun y2018d9() {
        assertEquals(422980L to 3552041936L, Day.testDay(Y2018D9::class))
    }

    @Test
    internal fun y2018d10() {
        assertEquals("LRCXFXRP" to 10630, Day.testDay(Y2018D10::class))
    }

    @Test
    internal fun y2018d11() {
        assertEquals("235,48" to "285,113,11", Day.testDay(Y2018D11::class))
    }

    @Test
    internal fun y2018d12() {
        assertEquals(4110 to 2650000000466, Day.testDay(Y2018D12::class))
    }

    @Test
    internal fun y2018d13() {
        assertEquals("86,118" to "2,81", Day.testDay(Y2018D13::class))
    }

    @Test
    internal fun y2018d14() {
        assertEquals(4910101614L to 20253137, Day.testDay(Y2018D14::class))
    }

    @Test
    internal fun y2018d15() {
        assertEquals(224370 to 45539, Day.testDay(Y2018D15::class))
    }

    @Test
    internal fun y2018d16() {
        assertEquals(529 to 573, Day.testDay(Y2018D16::class))
    }

    @Test
    internal fun y2018d17() {
        assertEquals(40879 to 34693, Day.testDay(Y2018D17::class))
    }

    @Test
    internal fun y2018d18() {
        assertEquals(605154 to 200364, Day.testDay(Y2018D18::class))
    }

    @Test
    internal fun y2018d19() {
        assertEquals(1764L to 18992484, Day.testDay(Y2018D19::class))
    }

//    @Test
//    internal fun y2018d20() {
//        assertEquals(3930 to 8240, Day.testDay(Y2018D20::class))
//    }

    @Test
    internal fun y2018d21() {
        assertEquals(3345459L to false, Day.testDay(Y2018D21::class, skipPartTwo = true))
    }

    @Test
    internal fun y2018d22() {
        assertEquals(5637 to 969, Day.testDay(Y2018D22::class))
    }

    @Test
    internal fun y2018d23() {
        assertEquals(481 to 47141479, Day.testDay(Y2018D23::class))
    }

    @Test
    internal fun y2018d24() {
        assertEquals(15165 to 4037, Day.testDay(Y2018D24::class))
    }

    @Test
    internal fun y2018d25() {
        assertEquals(394 to false, Day.testDay(Y2018D25::class))
    }
}
