package org.gristle.adventOfCode.utilities

// letterForms is each character rotated 90 degrees with newlines stripped out. The 90 degree rotation makes it
// easier to parse letters because they can be easily separated. 
private val letterForms = buildMap<String, Char> {
    // 6 height letters
    put("#####...#..#..#..######.", 'A')
    put("#######..#.##..#.#.##.#.", 'B')
    put(".####.#....##....#.#..#.", 'C')
    put("#######..#.##..#.##....#", 'E')
    put("######...#.#...#.#.....#", 'F')
    put(".####.#....##.#..####.#.", 'G')
    put("######...#.....#..######", 'H')
    put("#....########....#", 'I')
    put(".#....#.....#....#.#####", 'J')
    put("######...#...##.#.#....#", 'K')
    put("#######.....#.....#.....", 'L')
    put(".####.#....##....#.####.", 'O')
    put("######..#..#..#..#...##.", 'P')
    put("######..#..#.##..##..##.", 'R')
    put("#..##.#.#..##.#..#.#...#", 'S')
    put(".######.....#......#####", 'U')
    put("....##...#..###......#......##", 'Y')
    put("##...##.#..##..#.##...##", 'Z')
    // 10 height letters
    put("########......#...#.....#....#....#....#....#...#.########..", 'A')
    put("###########....#...##....#...##....#...##....#...#.####.###.", 'B')
    put(".########.#........##........##........##........#.#......#.", 'C')
    put("###########....#...##....#...##....#...##....#...##........#", 'E')
    put("##########.....#...#.....#...#.....#...#.....#...#.........#", 'F')
    put(".########.#........##........##...#....#.#..#....######...#.", 'G')
    put("##########.....#.........#.........#.........#....##########", 'H')
    put(".##.......#.........#.........#........#.#########.........#", 'J')
    put("##########....##.......#..#.....#....#...#......#.#........#", 'K')
    put("###########.........#.........#.........#.........#.........", 'L')
    put("##########.......##......##......##......##.......##########", 'N')
    put("##########.....#...#.....#...#.....#...#.....#...#......###.", 'P')
    put("##########.....#...#.....#...#....##...#..##.#...###....###.", 'R')
    put("##......##..##..##......##........##......##..##..##......##", 'X')
    put("###......##..#.....##...#....##....#...##.....#..##......###", 'Z')
}

fun Grid<Char>.ocr() = rotate90() // rotation makes for easy separation of each letter
    .addRight(Grid(width, 1) { '\n' }) // newline helps maintain shape of data through string transforms
    .joinToString("") // change into string to make replacements and splits easier
    .replace(' ', '.') // conform spaces into dots
    // Fixes bug where some 'Y' characters do not have a space between it and the next letter
    .replace("....##\n...#..\n###...\n...#..\n....##", "....##\n...#..\n###...\n...#..\n....##\n......\n")
    .split("""^(\.{6,}\n)+""".toRegex(RegexOption.MULTILINE)) // split each letter up into its own string
    .filter { it.isNotBlank() } // remove blank strings (found at end)
    .map { it.replace("\n", "") } // newlines not needed at this point
    .map { letterForms[it] ?: '?' } // map to Char
    .joinToString("") // put all Chars together into a String

@JvmName("ocrBoolean")
fun Grid<Boolean>.ocr() = mapToGrid { if (it) '#' else '.' }.ocr() 

fun String.ocr() = toGrid().ocr()