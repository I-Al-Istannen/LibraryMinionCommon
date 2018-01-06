package me.ialistannen.libraryminioncommon.fetching.goodreads

import me.ialistannen.libraryminioncommon.book.BookInformation
import me.ialistannen.libraryminioncommon.fetching.BookFetcher
import me.ialistannen.libraryminioncommon.isbn.Isbn

/**
 * Fetches information from goodreads.com.
 */
class GoodreadsBookFetcher : BookFetcher {

    override fun fetchInformation(isbn: Isbn): BookInformation? {
        val detailPage = DetailPageFinder.findDetailPage(isbn) ?: return null

        return DetailPageExtractor.extractFromDetailPage(detailPage)
    }
}