import kotlin.math.pow

typealias Springs = String
typealias Groups = List<Int>

fun Springs.match(groups: Groups): Boolean {
    return split(".").filter { it.isNotBlank() }.map { it.length } == groups
}

fun main() {
    fun Springs.alterations(groups: Groups): Int {
        val springs = this.dropWhile { it == '.' }.dropLastWhile { it == '.' }
        val blocks = springs.split(".").filter { it.isNotBlank() }
        return when {
            // if match is still incomplete but there aren't enough # left, stop
            springs.count { it == '?' || it == '#' } < groups.sum() -> {
                0
            }

            // if match is still incomplete but there aren't any # or ? left, stop
            (blocks.isEmpty() && groups.isNotEmpty()) -> {
                0
            }

            springs.count { it == '#' } > groups.sum() -> {
                0
            }

            springs.count { it == '#' } == 0 && groups.isEmpty() -> {
                1
            }
            // if block is too short, ...
            blocks.first().length < groups.first() -> {
                // ...drop it if it's all ? and move on to the next block
                if (blocks.first().all { it == '?' }) {
                    blocks.drop(1).joinToString(".").alterations(groups)
                }
                // ...the pattern can't match because the block contains not enough characters
                else {
                    0
                }
            }
            // if length of block is as expected,
            blocks.first().length == groups.first() -> {
                // all #, move on to next block
                if(blocks.first().any { it == '#' }) {
                    blocks.drop(1).joinToString(".").alterations(groups.drop(1))
                }
                else {
                    // assume first ? is a '#'
                    val firstSpringIsDamaged = springs.replaceFirst('?', '#')
                    val optionsA = firstSpringIsDamaged.alterations(groups)

                    // assume first ? is a '.'
                    val firstSpringIsOk = springs.replaceFirst('?', '.')
                    val optionsB = firstSpringIsOk.alterations(groups)

                    optionsA + optionsB
                }
            }

            // if the block is too long, ...
            blocks.first().length > groups.first() -> {
                // if it contains ? ...
                if (blocks.first().contains('?')) {
                    // check if all # are accounted for. They must be in a sequence.
                    val shouldFind = (0..<groups.first()).map { '#' }.joinToString("")
                    if (blocks.first().startsWith(shouldFind)) {
                        // and no ? are following ...
                        if (groups.first() + 1 >= blocks.first().length) {
                            blocks.drop(1).joinToString(".").alterations(groups.drop(1))
                        }
                        // and some ? are following ...
                        else {
                            if (blocks.first()[groups.first()] == '#') {
                                0
                            } else {
                                // Eliminate the #'s and the first ? from the block, the rest forms a new block, move on to next block
                                val newFirstBlock = blocks.first().substring(groups.first() + 1)
                                val newBlocks = blocks.drop(1).toMutableList()
                                newBlocks.add(0, newFirstBlock)
                                newBlocks.joinToString(".").alterations(groups.drop(1))
                            }
                        }

                    } else {
                        // assume first ? is a '#'
                        val firstSpringIsDamaged = springs.replaceFirst('?', '#')
                        val optionsA = firstSpringIsDamaged.alterations(groups)

                        // assume first ? is a '.'
                        val firstSpringIsOk = springs.replaceFirst('?', '.')
                        val optionsB = firstSpringIsOk.alterations(groups)
                        optionsA + optionsB
                    }
                }
                // if it doesn't contain any ? there are too many #, stop
                else {
                    0
                }
            }
            // this never happens
            else -> {
                0
            }
        }
    }

    fun part1(input: List<String>): Int {
        return input.mapIndexed { index, line ->
            line.split(" ").let { (left, right) ->
                left to (right.split(",").map { it.toInt() })
            }.let { (springs, groups) ->
                println(index)
                springs.alterations(groups)
            }
        }.also {
            println(it)
        }.sum()
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

    check(part2(testInput1) == 525152)

    //part2(input).println()
    //check(part2(input) == TODO())
}
