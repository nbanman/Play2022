package org.gristle.adventOfCode.utilities

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.y2021.d1.Y2021D1
import org.gristle.adventOfCode.y2021.d10.Y2021D10
import org.gristle.adventOfCode.y2021.d11.Y2021D11
import org.gristle.adventOfCode.y2021.d12.Y2021D12
import org.gristle.adventOfCode.y2021.d13.Y2021D13
import org.gristle.adventOfCode.y2021.d14.Y2021D14
import org.gristle.adventOfCode.y2021.d15.Y2021D15
import org.gristle.adventOfCode.y2021.d16.Y2021D16
import org.gristle.adventOfCode.y2021.d17.Y2021D17
import org.gristle.adventOfCode.y2021.d18.Y2021D18
import org.gristle.adventOfCode.y2021.d19.Y2021D19
import org.gristle.adventOfCode.y2021.d2.Y2021D2
import org.gristle.adventOfCode.y2021.d20.Y2021D20
import org.gristle.adventOfCode.y2021.d21.Y2021D21
import org.gristle.adventOfCode.y2021.d22.Y2021D22
import org.gristle.adventOfCode.y2021.d23.Y2021D23
import org.gristle.adventOfCode.y2021.d24.Y2021D24
import org.gristle.adventOfCode.y2021.d25.Y2021D25
import org.gristle.adventOfCode.y2021.d3.Y2021D3
import org.gristle.adventOfCode.y2021.d4.Y2021D4
import org.gristle.adventOfCode.y2021.d5.Y2021D5
import org.gristle.adventOfCode.y2021.d6.Y2021D6
import org.gristle.adventOfCode.y2021.d7.Y2021D7
import org.gristle.adventOfCode.y2021.d8.Y2021D8
import org.gristle.adventOfCode.y2021.d9.Y2021D9
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Test2021 {
    @Test
    internal fun y2021d1() {
        assertEquals(1342 to 1378, Day.testDay(Y2021D1::class))
    }

    @Test
    internal fun y2021d2() {
        assertEquals(2117664 to 2073416724, Day.testDay(Y2021D2::class))
    }

    @Test
    internal fun y2021d3() {
        assertEquals(3969000 to 4267809, Day.testDay(Y2021D3::class))
    }

    @Test
    internal fun y2021d4() {
        assertEquals(39902 to 26936, Day.testDay(Y2021D4::class))
    }

    @Test
    internal fun y2021d5() {
        assertEquals(5774 to 18423, Day.testDay(Y2021D5::class))
    }

    @Test
    internal fun y2021d6() {
        assertEquals(361169L to 1634946868992L, Day.testDay(Y2021D6::class))
    }

    @Test
    internal fun y2021d7() {
        assertEquals(343468 to 96086265, Day.testDay(Y2021D7::class))
    }

    @Test
    internal fun y2021d8() {
        assertEquals(397 to 1027422, Day.testDay(Y2021D8::class))
    }

    @Test
    internal fun y2021d9() {
        assertEquals(448 to 1417248, Day.testDay(Y2021D9::class))
    }

    @Test
    internal fun y2021d10() {
        assertEquals(167379L to 2776842859L, Day.testDay(Y2021D10::class))
    }

    @Test
    internal fun y2021d11() {
        assertEquals(1669 to 351, Day.testDay(Y2021D11::class))
    }

    @Test
    internal fun y2021d12() {
        assertEquals(4104 to 119760, Day.testDay(Y2021D12::class))
    }

    @Test
    internal fun y2021d13() {
        assertEquals(740 to "UFRZKAUZ", Day.testDay(Y2021D13::class))
    }

    @Test
    internal fun y2021d14() {
        assertEquals(3555L to 4439442043739L, Day.testDay(Y2021D14::class))
    }

    @Test
    internal fun y2021d15() {
        assertEquals(602 to 2935, Day.testDay(Y2021D15::class))
    }

    @Test
    internal fun y2021d16() {
        assertEquals(979 to 277110354175, Day.testDay(Y2021D16::class))
    }

    @Test
    internal fun y2021d17() {
        assertEquals(17766 to 1733, Day.testDay(Y2021D17::class))
    }

    @Test
    internal fun y2021d18() {
        assertEquals(3806L to 4727L, Day.testDay(Y2021D18::class))
    }

    @Test
    internal fun y2021d19() {
        assertEquals(370 to 13148, Day.testDay(Y2021D19::class))
    }

    @Test
    internal fun y2021d20() {
        assertEquals(5786 to 16757, Day.testDay(Y2021D20::class))
    }

    @Test
    internal fun y2021d21() {
        assertEquals(605070L to 218433063958910L, Day.testDay(Y2021D21::class))
    }

    @Test
    internal fun y2021d22() {
        assertEquals(587097L to 1359673068597669L, Day.testDay(Y2021D22::class))
    }

    @Test
    internal fun y2021d23() {
        assertEquals(14148 to 43814, Day.testDay(Y2021D23::class))
    }

    @Test
    internal fun y2021d24() {
        assertEquals(92969593497992L to 81514171161381L, Day.testDay(Y2021D24::class))
    }

    @Test
    internal fun y2021d25() {
        assertEquals(528 to false, Day.testDay(Y2021D25::class, skipPartTwo = true))
    }
}
