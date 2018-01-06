package me.ialistannen.libraryminioncommon.isbn

/**
 * Converts different ISBN types to each other.
 */
internal class Isbn13Converter {

    /**
     * Converts a **valid** [Isbn] to [IsbnType.ISBN_13].
     *
     * @param isbn the isbn to convert
     * @throws IllegalArgumentException if the passed isbn does not pass [Isbn.validate]
     * @return the converted isbn
     */
    fun toIsbn13(isbn: Isbn): Isbn {
        if (!isbn.validate()) {
            throw IllegalArgumentException("Isbn argument invalid: '$isbn'")
        }

        if (isbn.type == IsbnType.ISBN_13) {
            return isbn
        }

        val digits = isbn.stringForm.toCharArray()
                .dropLast(1)
                .map { Character.getNumericValue(it) }

        val digitsIsbn13: MutableList<Int> = mutableListOf(9, 7, 8).apply {
            addAll(digits)
            add(getCheckDigit(this))
        }

        return Isbn13(digitsIsbn13.joinToString(""))
    }

    /**
     * [See here for the algorithm](https://en.wikipedia.org/wiki/International_Standard_Book_Number#ISBN-10_to_ISBN-13_conversion)
     */
    private fun getCheckDigit(digits: List<Int>): Int {
        var sum = 0

        for ((index, digit) in digits.withIndex()) {
            var value = digit
            if (index % 2 != 0) {
                value *= 3
            }
            sum += value
        }

        return 10 - sum % 10
    }
}