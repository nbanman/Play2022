package benchmarks.y2016

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.y2016.d14.Y2016D14

@State(Scope.Benchmark)
class Y2016D14Bench {
    private lateinit var input: String

    @Setup
    fun prepare() {
        input = getInput(14, 2016)
    }

    @Benchmark
    fun part1(): Int = Y2016D14(input).part1()

    @Benchmark
    fun part2(): Int = Y2016D14(input).part2()
}