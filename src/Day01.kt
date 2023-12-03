fun main() {
    fun part1(input: List<String>): Int =
        input.map {
            it.first { it.isDigit() }.toString() + it.last { it.isDigit() }
        }.sumOf {
            it.toInt()
        }

    val digits = mapOf(
        "1" to "1",
        "2" to "2",
        "3" to "3",
        "4" to "4",
        "5" to "5",
        "6" to "6",
        "7" to "7",
        "8" to "8",
        "9" to "9",
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
    )

    fun String.calibrationValue(): String =
        digits[findAnyOf(digits.keys, 0)!!.second] +
                digits[findLastAnyOf(digits.keys, length - 1)!!.second]


    fun part2(input: List<String>): Int = input.map { line ->
        line.calibrationValue().toInt()
    }.sum()

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day01_test")
    part1(testInput1).println()
    check(part1(testInput1) == 142)

    val input = readInput("Day01")
    part1(input).println()
    check(part1(input) == 53921)

    val testInput2 = readInput("Day01_test2")
    part2(testInput2).println()
    check(part2(testInput2) == 281)

    part2(input).println()
    check(part2(input) == 54676)
}
