import java.lang.Math.pow
import java.util.*
import kotlin.math.exp
import kotlin.math.pow


typealias Springs = String
typealias Groups = List<Int>

fun Springs.match(groups: Groups): Boolean {
    return split(".").filter { it.isNotBlank() }.map { it.length } == groups
}

fun main() {

    fun checkPermutation() {

    }
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            line.split(" ").let { (left, right) ->
                left to (right.split(",").map { it.toInt() })
            }.let { (springs, groups) ->
                val numberOfWildcards = springs.count { it == '?' }
                val combinations = 2.toDouble().pow(numberOfWildcards).toInt()
                val permutations = (0..<combinations).map {
                    Integer.toBinaryString(it).padStart(numberOfWildcards, '0')
                }.map {
                    it.replace('0', '.').replace('1', '#')
                }

                permutations.count { permutation ->
                    var s = springs
                    springs.mapIndexedNotNull { index, c ->
                        if (c == '?') index else null
                    }.forEachIndexed { index, i ->
                        s = s.replaceRange(i, i + 1, permutation[index].toString())
                    }
                    s.match(groups)
                }
            }
        }
    }


    fun part2(input: List<String>): Int {
        return input.map {
            it.split(" ").let { (l, r) ->
                "$l?$l?$l?$l?$l $r,$r,$r,$r,$r"
            }
        }.let {
            part1(it)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day12_test")
    part1(testInput1).println()
    check(part1(testInput1) == 21)

    val input = readInput("Day12")
    part1(input).println()
    check(part1(input) == 6827)

    part2(testInput1).println()
    check(part2(testInput1) == TODO())

    part2(input).println()
    check(part2(input) == TODO())
}
