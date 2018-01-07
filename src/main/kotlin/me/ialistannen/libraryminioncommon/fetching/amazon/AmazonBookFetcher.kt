package me.ialistannen.libraryminioncommon.fetching.amazon

import me.ialistannen.libraryminioncommon.book.BookInformation
import me.ialistannen.libraryminioncommon.fetching.BookFetcher
import me.ialistannen.libraryminioncommon.isbn.Isbn

/**
 * Fetches [BookInformation] from Amazon.
 */
class AmazonBookFetcher : BookFetcher {

    override fun fetchInformation(isbn: Isbn): BookInformation? {
        val detailPage = DetailPageFinder.findDetailPage(isbn) ?: return null

        return DetailPageExtractor.extractFromDetailPage(detailPage)
    }
}