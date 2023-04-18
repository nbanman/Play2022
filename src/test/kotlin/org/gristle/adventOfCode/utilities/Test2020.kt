package org.gristle.adventOfCode.utilities

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.y2020.d1.Y2020D1
import org.gristle.adventOfCode.y2020.d10.Y2020D10
import org.gristle.adventOfCode.y2020.d11.Y2020D11
import org.gristle.adventOfCode.y2020.d12.Y2020D12
import org.gristle.adventOfCode.y2020.d13.Y2020D13
import org.gristle.adventOfCode.y2020.d14.Y2020D14
import org.gristle.adventOfCode.y2020.d15.Y2020D15
import org.gristle.adventOfCode.y2020.d16.Y2020D16
import org.gristle.adventOfCode.y2020.d17.Y2020D17
import org.gristle.adventOfCode.y2020.d18.Y2020D18
import org.gristle.adventOfCode.y2020.d19.Y2020D19
import org.gristle.adventOfCode.y2020.d2.Y2020D2
import org.gristle.adventOfCode.y2020.d20.Y2020D20
import org.gristle.adventOfCode.y2020.d21.Y2020D21
import org.gristle.adventOfCode.y2020.d22.Y2020D22
import org.gristle.adventOfCode.y2020.d23.Y2020D23
import org.gristle.adventOfCode.y2020.d24.Y2020D24
import org.gristle.adventOfCode.y2020.d25.Y2020D25
import org.gristle.adventOfCode.y2020.d3.Y2020D3
import org.gristle.adventOfCode.y2020.d4.Y2020D4
import org.gristle.adventOfCode.y2020.d5.Y2020D5
import org.gristle.adventOfCode.y2020.d6.Y2020D6
import org.gristle.adventOfCode.y2020.d7.Y2020D7
import org.gristle.adventOfCode.y2020.d8.Y2020D8
import org.gristle.adventOfCode.y2020.d9.Y2020D9
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Test2020 {
    @Test
    internal fun y2020d1() {
        assertEquals(1015476 to 200878544, Day.testDay(Y2020D1::class))
    }

    @Test
    internal fun y2020d2() {
        assertEquals(445 to 491, Day.testDay(Y2020D2::class))
    }

    @Test
    internal fun y2020d3() {
        assertEquals(294 to 5774564250, Day.testDay(Y2020D3::class))
    }

    @Test
    internal fun y2020d4() {
        assertEquals(242 to 186, Day.testDay(Y2020D4::class))
    }

    @Test
    internal fun y2020d5() {
        assertEquals(922 to 747, Day.testDay(Y2020D5::class))
    }

    @Test
    internal fun y2020d6() {
        assertEquals(6297 to 3158, Day.testDay(Y2020D6::class))
    }

    @Test
    internal fun y2020d7() {
        assertEquals(252 to 35487L, Day.testDay(Y2020D7::class))
    }

    @Test
    internal fun y2020d8() {
        assertEquals(1915 to 944, Day.testDay(Y2020D8::class))
    }

    @Test
    internal fun y2020d9() {
        assertEquals(552655238L to 70672245L, Day.testDay(Y2020D9::class))
    }

    @Test
    internal fun y2020d10() {
        assertEquals(1890 to 49607173328384, Day.testDay(Y2020D10::class))
    }

    @Test
    internal fun y2020d11() {
        assertEquals(2243 to 2027, Day.testDay(Y2020D11::class))
    }

    @Test
    internal fun y2020d12() {
        assertEquals(2280 to 38693, Day.testDay(Y2020D12::class))
    }

    @Test
    internal fun y2020d13() {
        assertEquals(115L to 756261495958122L, Day.testDay(Y2020D13::class))
    }

    @Test
    internal fun y2020d14() {
        assertEquals(11926135976176 to 4330547254348, Day.testDay(Y2020D14::class))
    }

    @Test
    internal fun y2020d15() {
        assertEquals(929 to 16671510, Day.testDay(Y2020D15::class))
    }

    @Test
    internal fun y2020d16() {
        assertEquals(29878 to 855438643439, Day.testDay(Y2020D16::class))
    }

    @Test
    internal fun y2020d17() {
        assertEquals(346 to 1632, Day.testDay(Y2020D17::class))
    }

    @Test
    internal fun y2020d18() {
        assertEquals(510009915468 to 321176691637769, Day.testDay(Y2020D18::class))
    }

    @Test
    internal fun y2020d19() {
        assertEquals(151 to 386, Day.testDay(Y2020D19::class))
    }

    @Test
    internal fun y2020d20() {
        assertEquals(19955159604613 to 1639, Day.testDay(Y2020D20::class))
    }

    @Test
    internal fun y2020d21() {
        assertEquals(2493 to "kqv,jxx,zzt,dklgl,pmvfzk,tsnkknk,qdlpbt,tlgrhdh", Day.testDay(Y2020D21::class))
    }

    @Test
    internal fun y2020d22() {
        assertEquals(32824 to 36515, Day.testDay(Y2020D22::class))
    }

    @Test
    internal fun y2020d23() {
        assertEquals(94238657 to 3072905352L, Day.testDay(Y2020D23::class))
    }

    @Test
    internal fun y2020d24() {
        assertEquals(244 to 3665, Day.testDay(Y2020D24::class))
    }

    @Test
    internal fun y2020d25() {
        assertEquals(296776L to false, Day.testDay(Y2020D25::class, skipPartTwo = true))
    }
}
