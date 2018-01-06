package me.ialistannen.libraryminioncommon.isbn

/**
 * An [Isbn] of the type [IsbnType.ISBN_13]
 */
data class Isbn13(
        override val stringForm: String

) : Isbn {

    override val type: IsbnType
        get() = IsbnType.ISBN_13

    override fun validate(): Boolean {
        if (stringForm.length != 13) {
            return false
        }
        // https://en.wikipedia.org/wiki/International_Standard_Book_Number#ISBN-10_to_ISBN-13_conversion
        var sum = 0

        val digits = stringForm.toCharArray().map { Character.getNumericValue(it) }

        for ((index, digit) in digits.dropLast(1).withIndex()) {
            var value = digit
            if (index % 2 != 0) {
                value *= 3
            }
            sum += value
        }

        return (sum + digits.last()) % 10 == 0
    }
}