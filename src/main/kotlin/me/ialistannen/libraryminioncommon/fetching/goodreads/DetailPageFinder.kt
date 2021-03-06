package me.ialistannen.libraryminioncommon.fetching.goodreads

import me.ialistannen.libraryminioncommon.fetching.io.WebsiteFetcher
import me.ialistannen.libraryminioncommon.isbn.Isbn
import org.jsoup.nodes.Document
import java.io.IOException

/**
 * Finds the HTML code for the detail page.
 */
internal object DetailPageFinder {

    private val BASE_URL = "https://www.goodreads.com/search/?q=%s"

    /**
     * Finds the html for the book's detail page.
     *
     * @param isbn the isbn of the book
     * @return the fetched document, if any
     */
    fun findDetailPage(isbn: Isbn): Document? {
        val url = BASE_URL.format(isbn.stringForm)

        try {
            val document = WebsiteFetcher.fetchPage(url)

            // no search result found
            if (document.baseUri().contains(".com/search/")) {
                return null
            }
            return document
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}