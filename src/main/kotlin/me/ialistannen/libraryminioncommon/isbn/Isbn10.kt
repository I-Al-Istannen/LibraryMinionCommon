package me.ialistannen.libraryminioncommon.isbn

/**
 * An [Isbn] of the type [IsbnType.ISBN_10]
 */
data class Isbn10(
        override val stringForm: String

) : Isbn {

    override val type: IsbnType
        get() = IsbnType.ISBN_10

    override fun validate(): Boolean {
        // https://en.wikipedia.org/wiki/International_Standard_Book_Number#ISBN-10_check_digits
        if (stringForm.length != 10) {
            return false
        }

        val digits: MutableList<Int> = stringForm.toCharArray()
                .dropLast(1)
                .map { Character.getNumericValue(it) }
                .toMutableList()

        if (stringForm.last() == 'X') {
            digits.add(10)
        } else {
            digits.add(Character.getNumericValue(stringForm.last()))
        }

        // iterate the factor in reverse (10 9 8 7 6 5 4 3 2 1)
        val sum = digits.withIndex().map { (10 - it.index) * it.value }.sum()

        return sum % 11 == 0
    }
}