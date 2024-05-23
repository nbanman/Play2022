package org.gristle.adventOfCode.y2023.d1

import org.gristle.adventOfCode.Day

class Y2023D1(input: String) : Day {

    private val lines = input.lines()

    // searches the line forwards and backwards looking for the first Char that is a digit, combines and 
    // sums them.
    override fun part1() = lines.sumOf { "${it.first(Char::isDigit)}${it.last(Char::isDigit)}".toInt() }

    // For fun, part2 pursues a baroque strategy. Two tries are created, one for searching forwards, one backwards.
    // Each node is assigned a value and a map containing potential children, eventually spelling out the names of 
    // digits. So 'o' -> 'n' -> 'e' and 'f' -> ['o' -> 'u' -> 'r'] | ['i' -> 'v' -> 'e']. The fun 'getDigit' takes
    // a line and goes through each character, trying to traverse a trie to a leaf. If at any time a numerical digit
    // is encountered, the function immediately returns the value of the digit. Otherwise, it goes to the end. Any
    // time a word does not match it is immediately discarded.
    override fun part2(): Int {

        // One trie contains information for searching forwards, the other for searching backwards. 
        // E.g., "one" becomes "eno"
        val forwardTrie = Node(0)
        val reverseTrie = Node(0)

        val numbers = "one|two|three|four|five|six|seven|eight|nine".splitToSequence('|')
        numbers
            .forEachIndexed { index, s ->
                // get value of the number from the ordinal order of the list
                val value = index + 1

                // populate the tries with nodes relating to the digit
                forwardTrie.populateTrie(s, value)
                reverseTrie.populateTrie(s.reversed(), value)
            }

        return lines.sumOf { line ->
            val firstDigit = getDigit(line, forwardTrie, false) // search forward
            val secondDigit = getDigit(line, reverseTrie, true) // search backward
            firstDigit * 10 + secondDigit
        }
    }

    class Node(value: Int, val children: MutableMap<Char, Node> = mutableMapOf()) {
        private val _value: Int = value
        val value: Int get() = if (children.isEmpty()) _value else 0
    }

    // See pt2 description for how this works.
    private fun getDigit(line: String, trie: Node, reversed: Boolean): Int {

        // search forwards or backwards
        val range = if (reversed) {
            line.lastIndex downTo 0
        } else {
            line.indices
        }

        // tracks snippets of sequences that may end up being spelled out digits
        var potentials: List<Node> = emptyList()

        // traverses string, forwards or backwards
        range.forEach { index ->
            val token = line[index] // get next character
                .also { if (it.isDigit()) return it.digitToInt() } // returns early if numerical digit found

            // starts a new search starting at the current token. Stored as a list for easy addition later.
            val current = trie.children[token]
                ?.also { if (it.value != 0) return it.value } // returns early if numerical digit found
                ?.let { listOf(it) } // store as list
                ?: emptyList()

            // for each potential still in play, advance it along the trie, discarding ones that don't match
            val evaluated = potentials.mapNotNull { node ->
                node.children[token] // moves it along. if it doesn't match, it returns null, which gets discarded
                    ?.also { if (it.value != 0) return it.value } // returns if leaf found, meaning a digit was spelled
            }

            // update the list of potential words for the next pass
            potentials = evaluated + current
        }

        // if the string does not contain a numerical or spelled-out digit, the string is malformed so throw 
        throw IllegalArgumentException("No digit found in $line")
    }

    // Populates the trie with nodes corresponding to the string.
    private fun Node.populateTrie(s: String, value: Int) {
        s.fold(this) { previous, c -> previous.children.getOrPut(c) { Node(value) } }
    }
}

fun main() = Day.runDay(Y2023D1::class)

//    Class creation: 15ms
//    Part 1: 54388 (2ms)
//    Part 2: 53515 (9ms)
//    Total time: 27ms