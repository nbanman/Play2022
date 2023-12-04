import org.gristle.adventOfCode.Day

class Y2023D3b(input: String) : Day {
    private fun Sequence<String>.solve(
        sum: Sequence<List<Int>>.() -> Int,
    ) = windowed(3) { lines ->
        symbols.findAll(lines[1])
            .map(MatchResult::range)
            .map(IntRange::first)
            .map { symbol ->
                lines.flatMap(digits::findAll)
                    .map { (it.range.first - 1)..(it.range.last + 1) to it.groupValues[1].toInt() }
                    .filter { (target, _) -> symbol in target }.map { (_, number) -> number }
            }
    }.flatten().run(sum)

    private val digits = "([0-9]+)".toRegex()
    private val symbols = "([^0-9.])".toRegex()
    private val schematics = input.lineSequence()

    override fun part1() = schematics.solve { sumOf { it.sum() } }

    override fun part2() = schematics.solve { filter { it.size == 2 }.sumOf { (a, b) -> a * b } }
}

fun main() = Day.runDay(Y2023D3b::class)