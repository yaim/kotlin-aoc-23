enum class Card(val c: Char) {
    ACE('A'),
    KING('K'),
    QUEEN('Q'),
    JACK('J'),
    TEN('T'),
    NINE('9'),
    EIGHT('8'),
    SEVEN('7'),
    SIX('6'),
    FIVE('5'),
    FOUR('4'),
    THREE('3'),
    TWO('2'),
    JOKER('*');

    companion object {
        private val map = entries.associateBy { it.c }

        fun fromChar(c: Char) = map[c]!!
    }
}

enum class HandStrength {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD;

    companion object {
        fun determine(cards: List<Card>): HandStrength {
            val values = cards.filter { it != Card.JOKER }.groupingBy { it }.eachCount().values.sorted().reversed()
            val jokerCount = cards.count { it == Card.JOKER }

            val a = values.getOrNull(0) ?: 0
            val b = values.getOrNull(1) ?: 0

            if (a + jokerCount == 5) return FIVE_OF_A_KIND
            if (a + jokerCount == 4) return FOUR_OF_A_KIND
            if (a + jokerCount == 3 && b == 2) return FULL_HOUSE
            if (a + jokerCount == 3) return THREE_OF_A_KIND
            if (a + jokerCount == 2 && b == 2) return TWO_PAIR
            if (a + jokerCount == 2) return ONE_PAIR

            return HIGH_CARD
        }
    }

}

fun main() {
    data class Hand(val cards: List<Card>, val bid: Int) {
        val strength = HandStrength.determine(cards)
    }

    fun String.parse() = Hand(
        substringBefore(' ').map { Card.fromChar(it) },
        substringAfter(' ').toInt()
    )

    fun List<Hand>.sorted() = sortedWith { h1, h2 ->
        if (h1.strength > h2.strength) return@sortedWith 1
        if (h2.strength > h1.strength) return@sortedWith -1

        h1.cards.forEachIndexed { index, h1c ->
            val h2c = h2.cards[index]

            if (h1c > h2c) return@sortedWith 1
            if (h2c > h1c) return@sortedWith -1
        }

        0
    }

    fun part1(input: List<String>): Int {
        val count = input.size

        return input
            .map { it.parse() }.sorted().withIndex()
            .sumOf { (index, hand) -> (count - index) * hand.bid }
    }

    fun part2(input: List<String>): Int {
        val count = input.size

        return input
            .map { it.replace('J', '*').parse() }.sorted().withIndex()
            .sumOf { (index, hand) -> (count - index) * hand.bid }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day07_test_1")) == 6440)
    check(part2(readInput("Day07_test_1")) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
