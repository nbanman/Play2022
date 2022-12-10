package benchmarks.y2022.d1

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.y2022.d1.Y2022D1

@State(Scope.Benchmark)
class Y2022D1Bench {
    private lateinit var input: String

    @Setup
    fun prepare() {
        input = getInput(1, 2022)
    }

    @Benchmark
    fun part1(): Int = Y2022D1(input).part1()

    @Benchmark
    fun part2(): Int = Y2022D1(input).part2()
}