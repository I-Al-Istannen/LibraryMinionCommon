package me.ialistannen.libraryminioncommon.book

import me.ialistannen.libraryminioncommon.isbn.Isbn

/**
 * Contains information about a book.
 */
data class BookInformation(
        val isbn: Isbn,
        val title: String,
        val author: List<Pair<String, String>> = emptyList(),
        val rating: Double? = null,
        val pageCount: Int? = null,
        val description: String? = null,
        val price: CurrencyValue? = null,
        val language: String? = null,
        val publisher: String? = null,
        val coverType: String? = null,
        val extra: Map<String, String> = emptyMap()
)