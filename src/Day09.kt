import java.util.*

fun main() {

    fun extrapolate(input: List<List<Int>>): Int {
        return input.sumOf { line ->
            var sensorData = line
            val lastValues = mutableListOf<Int>()
            while (sensorData.any { it != 0 }) {
                lastValues.add(0, sensorData.last())
                sensorData = sensorData.zipWithNext().map { (a, b) -> b - a }
            }
            lastValues.reduce { acc, i -> acc + i }
        }
    }

    fun part1(input: List<String>): Int = input.map { line ->
        line.split(" ").map { it.toInt() }
    }.let { extrapolate(it) }

    fun part2(input: List<String>): Int = input.map { line ->
        line.split(" ").map { it.toInt() }.reversed()
    }.let { extrapolate(it) }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day09_test")
    part1(testInput1).println()
    check(part1(testInput1) == 114)

    val input = readInput("Day09")
    part1(input).println()
    check(part1(input) == 1938800261)

    part2(testInput1).println()
    check(part2(testInput1) == 2)

    part2(input).println()
    check(part2(input) == 1112)
}
