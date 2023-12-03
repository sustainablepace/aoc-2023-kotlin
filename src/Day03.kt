import kotlin.math.abs

typealias Number = Pair<Int, NumberPosition>
typealias NumberPosition = Pair<Int, IntRange>
typealias SymbolPosition = Pair<Int, Int>

fun SymbolPosition.isAdjacentTo(numberPosition: NumberPosition) : Boolean {
    numberPosition.second.forEach {
        if(abs(first - numberPosition.first) <= 1 && abs(second - it) <= 1) {
            return true
        }
    }
    return false
}
fun main() {
    fun part1(input: List<String>): Int {
        val symbols = input.flatMapIndexed { row, line ->
            line.mapIndexedNotNull { column, ch ->
                if(!ch.isDigit() && ch != '.') {
                    row to column
                } else null
            }
        }
        val pattern = Regex("[0-9]+")
        return input.flatMapIndexed { row: Int, line: String ->
            val match = pattern.findAll(line)
            match.map {
                it.value.toInt() to (row to it.range)
            }
        }.filter { number: Number ->
            symbols.any {
                it.isAdjacentTo(number.second)
            }
        }.sumOf {
            it.first
        }
    }


    fun part2(input: List<String>): Int {
        val gears = input.flatMapIndexed { row, line ->
            line.mapIndexedNotNull { column, ch ->
                if(ch == '*') {
                    row to column
                } else null
            }
        }
        val pattern = Regex("[0-9]+")
        val numbers = input.flatMapIndexed { row: Int, line: String ->
            val match = pattern.findAll(line)
            match.map {
                it.value.toInt() to (row to it.range)
            }
        }

        return gears.mapNotNull { gear ->
            numbers.filter { number ->
                gear.isAdjacentTo(number.second)
            }.let {
                if (it.size == 2) {
                    it[0].first * it[1].first
                } else null
            }
        }.sum()

    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day03_test")
    part1(testInput1).println()
    check(part1(testInput1) == 4361)

    val input = readInput("Day03")
    part1(input).println()
    check(part1(input) == 527369)

    part2(testInput1).println()
    check(part2(testInput1) == 467835)

    part2(input).println()
    check(part2(input) == 73074886)
}
