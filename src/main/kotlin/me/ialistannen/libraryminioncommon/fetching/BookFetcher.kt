package me.ialistannen.libraryminioncommon.fetching

import me.ialistannen.libraryminioncommon.book.BookInformation
import me.ialistannen.libraryminioncommon.isbn.Isbn

/**
 * Fetches books from some online source.
 */
interface BookFetcher {

    /**
     * Fetches information about a book via the passed [Isbn].
     *
     * @param isbn the isbn to fetch them for
     *
     * @return the fetched book information, if any
     */
    fun fetchInformation(isbn: Isbn): BookInformation?
}