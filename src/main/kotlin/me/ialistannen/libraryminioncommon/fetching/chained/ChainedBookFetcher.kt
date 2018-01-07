package me.ialistannen.libraryminioncommon.fetching.chained

import me.ialistannen.libraryminioncommon.book.BookInformation
import me.ialistannen.libraryminioncommon.fetching.BookFetcher
import me.ialistannen.libraryminioncommon.isbn.Isbn

/**
 * Chains multiple [BookFetcher] after each other, in the order they are provided.
 */
class ChainedBookFetcher(private val fetchers: List<BookFetcher>) : BookFetcher {

    override fun fetchInformation(isbn: Isbn): BookInformation? {
        @Suppress("LoopToCallChain")     // I want lazy evaluation!
        for (fetcher in fetchers) {
            val bookInformation = fetcher.fetchInformation(isbn)
            if (bookInformation != null) {
                return bookInformation
            }
        }

        return null
    }
}