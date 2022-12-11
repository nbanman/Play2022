package benchmarks.y2022.d1

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.y2022.d2.Y2022D2

@State(Scope.Benchmark)
class Y2022D2Bench {
    private lateinit var input: String

    @Setup
    fun prepare() {
        input = getInput(2, 2022)
    }

    @Benchmark
    fun part1(): Int = Y2022D2(input).part1()

    @Benchmark
    fun part2(): Int = Y2022D2(input).part2()
}