open class Hand(val cards: String) {

    open val cardValue = mapOf(
        'A' to 14,
        'K' to 13,
        'Q' to 12,
        'J' to 11,
        'T' to 10,
        '9' to 9,
        '8' to 8,
        '7' to 7,
        '6' to 6,
        '5' to 5,
        '4' to 4,
        '3' to 3,
        '2' to 2
    )
    open fun cardValue(): Int =
        15 * 15 * 15 * 15 * cardValue[cards[0]]!! + 15 * 15 * 15 * cardValue[cards[1]]!! + 15 * 15 * cardValue[cards[2]]!! + 15 * cardValue[cards[3]]!! + cardValue[cards[4]]!!
    override fun toString(): String {
        return cards + " " + valueOf()
    }

    open fun valueOf(): Int {
        return if (cards.groupBy { it }.count() == 1) { // Five of a kind
            60_000_000 + cardValue()
        } else if (cards.groupBy { it }.count() == 2) {
            if (cards.groupBy { it }.any { it.value.count() == 4 }) { // Four of a kind
                50_000_000 + cardValue()
            } else { // Full House
                40_000_000 + cardValue()
            }
        } else if (cards.groupBy { it }.count() == 3) { // Three of a kind or two pair
            if (cards.groupBy { it }.any { it.value.count() == 3 }) { // Three of a kind
                30_000_000 + cardValue()
            } else {
                20_000_000 + cardValue()
            }
        } else if (cards.groupBy { it }.count() == 4) { // One pair
            10_000_000 + cardValue()
        } else {
            cardValue()
        }
    }
}

class HandWithJokers(cards: String): Hand(cards) {
    override val cardValue = mapOf(
        'A' to 14,
        'K' to 13,
        'Q' to 12,
        'T' to 10,
        '9' to 9,
        '8' to 8,
        '7' to 7,
        '6' to 6,
        '5' to 5,
        '4' to 4,
        '3' to 3,
        '2' to 2,
        'J' to 1
    )
    override fun cardValue(): Int =
        15 * 15 * 15 * 15 * cardValue[cards[0]]!! + 15 * 15 * 15 * cardValue[cards[1]]!! + 15 * 15 * cardValue[cards[2]]!! + 15 * cardValue[cards[3]]!! + cardValue[cards[4]]!!

    override fun valueOf(): Int {
        val cardsWithoutJokers = cards.replace("J", "")
        return if (cardsWithoutJokers.groupBy { it }.count() <= 1) { // Five of a kind
            60_000_000 + cardValue()
        } else if (cardsWithoutJokers.groupBy { it }.count() == 2) {
            if (cardsWithoutJokers.groupBy { it }.any { 5 - cardsWithoutJokers.count() + it.value.count() == 4 }) { // Four of a kind
                50_000_000 + cardValue()
            } else {
                40_000_000 + cardValue()
            }
        } else if (cardsWithoutJokers.groupBy { it }.count() == 3) { // Three of a kind or two pair
            if (cardsWithoutJokers.groupBy { it }.any { 5 - cardsWithoutJokers.count() + it.value.count() == 3 }) { // Three of a kind
                30_000_000 + cardValue()
            } else {
                20_000_000 + cardValue()
            }
        } else if (cardsWithoutJokers.groupBy { it }.count() == 4) { // One pair
            10_000_000 + cardValue()
        } else {
            cardValue()
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            it.split(" ").let { (hand, bid) ->
                Hand(hand) to bid.toInt()
            }
        }.sortedBy {
            it.first.valueOf()
        }.mapIndexed { index, (_, bid) ->
            (index + 1) * bid
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.map {
            it.split(" ").let { (hand, bid) ->
                HandWithJokers(hand) to bid.toInt()
            }
        }.sortedBy {
            it.first.valueOf()
        }.mapIndexed { index, (_, bid) ->
            (index + 1) * bid
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day07_test")
    part1(testInput1).println()
    check(part1(testInput1) == 6440)

    val input = readInput("Day07")
    part1(input).println()
    check(part1(input) == 248569531)

    part2(testInput1).println()
    check(part2(testInput1) == 5905)

    part2(input).println()
    check(part2(input) == 250382098)
}
