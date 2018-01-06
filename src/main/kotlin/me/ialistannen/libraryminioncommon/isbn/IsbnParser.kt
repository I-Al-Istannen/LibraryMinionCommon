package me.ialistannen.libraryminioncommon.isbn

/**
 * Parses [Isbn]s.
 */
object IsbnParser {

    private val sanitationPattern = Regex("[^0-9X]")

    /**
     * Parses a passed string to an [Isbn], if possible.
     *
     * Does some sanitation, removes all characters that can not appear in a valid isbn.
     *
     * @param isbnString the isbn string to parse
     *
     * @return the isbn or null if it was not a valid one
     */
    fun parseIsbn(isbnString: String): Isbn? {
        val sanitizedString = sanitizeIsbnString(isbnString)

        val isbn: Isbn = when (sanitizedString.length) {
            10   -> Isbn10(sanitizedString)
            13   -> Isbn13(sanitizedString)
            else -> return null
        }

        if (!isbn.validate()) {
            return null
        }

        return isbn
    }

    private fun sanitizeIsbnString(isbnString: String): String {
        return isbnString.replace(sanitationPattern, "")
    }
}