import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

val digitStrings = listOf(
    "zero",
    "one",
    "two",
    "three",
    "four",
    "five",
    "six",
    "seven",
    "eight",
    "nine",
)

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun String.firstDigitChar(): Char = this.toCharArray().first { it.isDigit() }

fun String.filterDigits(): String {
    val digits = StringBuilder()

    for (i in indices) {
        if (this[i].isDigit()) digits.append(this[i])
        digitStrings.forEachIndexed { digit, str -> if (indexOf(str, i) == i) digits.append(digit) }
    }

    return digits.toString()
}

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
