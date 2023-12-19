package org.gristle.adventOfCode.y2023.d19

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.blankSplit
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.gvs

class Y2023D19(input: String) : Day {
    
    data class Workflow(
        val category: Int,
        val amount: Int,
        val comparison: String,
        val destination: String
    ) {
        fun test(part: List<Int>) = if (comparison == ">") {
            if (part[category] > amount) destination else ""
        } else if (comparison == "<") {
            if (part[category] < amount) destination else ""
        } else {
            destination
        }
    }
    
    private fun List<Workflow>.route(partRanges: PartRanges): List<Pair<String, PartRanges>> = buildList {
        var remaining: PartRanges? = partRanges
        for (workflow in this@route) {
            if (remaining == null) return@buildList
            remaining = if (workflow.comparison.isNotEmpty()) {
                val (pass, fail) = remaining.split(workflow)
                if (pass.ranges[workflow.category].first <= pass.ranges[workflow.category].last) {
                    add(workflow.destination to pass)
                }
                if (fail.ranges[workflow.category].first <= fail.ranges[workflow.category].last) {
                    fail
                } else {
                    null
                }
            } else {
                add(workflow.destination to remaining)
                null
            }
        }
    }
    
    @JvmInline
    value class PartRanges(val ranges: List<IntRange>) {
        fun permutations(): Long = ranges.fold(1L) { acc, range -> acc * (1 + range.last - range.first) }
        
        // two lists, one that matches, and one that doesn't match
        fun split(workflow: Workflow): List<PartRanges> {
            return when (workflow.comparison) {
                ">" -> {
                    val breakpoint = workflow.amount + 1
                    val pass = breakpoint..ranges[workflow.category].last
                    val fail = ranges[workflow.category].first until breakpoint
                    makeCopies(workflow.category, pass, fail)
                }
                "<" -> {
                    val breakpoint = workflow.amount 
                    val pass = ranges[workflow.category].first until breakpoint
                    val fail = breakpoint..ranges[workflow.category].last
                    makeCopies(workflow.category, pass, fail)
                }
                else -> throw IllegalArgumentException("Non-comparisons should not be passed to split function.")
            }
        }
        
        private fun makeCopies(category: Int, pass: IntRange, fail: IntRange): List<PartRanges> =
            listOf(
                PartRanges(List(4) { i -> if (i == category) pass else ranges[i] }),
                PartRanges(List(4) { i -> if (i == category) fail else ranges[i] }),
            )
    } 
    
    private val workflows: Map<String, List<Workflow>>
    private val parts: List<List<Int>>
    
    init {
        val (workStanza, partStanza) = input.blankSplit()
        workflows = workStanza
            .lines().associate { line ->
                val name = line.takeWhile { it.isLetter() }
                val workflows = line.dropWhile { it != '{' }
                    .gvs("""(?:([xmas])([<>])(\d+):)?(\w+)""")
                    .map { (categoryStr, comparison, amountStr, destination) ->
                        val category = when (categoryStr) {
                            "x" -> 0
                            "m" -> 1
                            "a" -> 2
                            "s" -> 3
                            else -> 0
                        }
                        val amount = amountStr.toIntOrNull() ?: 0
                        Workflow(category, amount, comparison, destination)
                    }.toList()
                name to workflows
            }
        parts = partStanza.getInts().chunked(4).toList()
    }
    
    private fun sort(name: String, part: List<Int>): String {
        val workflow = workflows.getValue(name)
        for (conditional in workflow) {
            val result = conditional.test(part)
            if (result == "A" || result == "R") {
                return result
            } else if (result.isNotBlank()) {
                return sort(result, part)
            }
        }
        return ""
    }
    
    override fun part1() = parts
        .filter { xmas ->
            generateSequence("in") { sort(it, xmas) }
                .first { it == "A" || it == "R" }
                .let { it == "A"}
        }.sumOf { it.sum() }

    override fun part2(): Long {
        val accepted = mutableListOf<PartRanges>()
        var remaining: List<Pair<String, PartRanges>> = listOf("in" to PartRanges(List(4) { 1..4000 }))
        while (remaining.isNotEmpty()) {
            val next = remaining.flatMap { (name, partRanges) ->
                workflows.getValue(name).route(partRanges)
            }
            remaining = next.filter { (name, partRanges) ->
                when (name) {
                    "R" -> false
                    "A" -> {
                        accepted.add(partRanges)
                        false
                    }
                    else -> true
                }
            }
        }
        return accepted.fold(0L) { acc, partRanges -> acc + partRanges.permutations() }
    }
}

fun main() = Day.runDay(Y2023D19::class)

//    Class creation: 47ms
//    Part 1: 449531 (4ms)
//    Part 2: 122756210763577 (10ms)
//    Total time: 63ms


@Suppress("unused")
private val sampleInput = listOf(
    """px{a<2006:qkq,m>2090:A,rfg}
pv{a>1716:R,A}
lnx{m>1548:A,A}
rfg{s<537:gd,x>2440:R,A}
qs{s>3448:A,lnx}
qkq{x<1416:A,crn}
crn{x>2662:A,R}
in{s<1351:px,qqz}
qqz{s>2770:qs,m<1801:hdj,R}
gd{a>3333:R,R}
hdj{m>838:A,pv}

{x=787,m=2655,a=1222,s=2876}
{x=1679,m=44,a=2067,s=496}
{x=2036,m=264,a=79,s=2244}
{x=2461,m=1339,a=466,s=291}
{x=2127,m=1623,a=2188,s=1013}
""",
)