import java.util.*
import kotlin.math.pow

fun main() {

    fun List<String>.parse() = map { line ->
        line.split(":").let { (left, right) ->
            right.split("|").let { (winners, mine) ->
                winners.split(" ").filter { it.contains("[0-9]+".toRegex()) }.map { it.toInt() }.toSet() to
                        mine.split(" ").filter { it.contains("[0-9]+".toRegex()) }.map { it.toInt() }.toSet()
            }
        }
    }

    fun part1(input: List<String>): Int = input.parse().map { (winners, mine) ->
        winners.intersect(mine).size.let { numberOfWinners ->
            if (numberOfWinners > 0) {
                2.toDouble().pow(numberOfWinners - 1).toInt()
            } else 0
        }
    }.sumOf { it }


    fun part2(input: List<String>): Int = input.parse().map { (winners, mine) ->
        winners.intersect(mine).size
    }.let { winningNumbers ->
        winningNumbers.foldIndexed(MutableList(winningNumbers.size) { 1 }) { index, cards, winners ->
            val instancesOfThisCard = cards[index]
            val indexes = (1..winners).map { it + index }
            indexes.filter { it < cards.size }.forEach {
                cards[it] = cards[it] + instancesOfThisCard
            }
            cards
        }
    }.sum()

// test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day04_test")
    part1(testInput1).println()
    check(part1(testInput1) == 13)

    val input = readInput("Day04")
    part1(input).println()
    check(part1(input) == 21568)

    part2(testInput1).println()
    check(part2(testInput1) == 30)

    part2(input).println()
    check(part2(input) == 11827296)
}
