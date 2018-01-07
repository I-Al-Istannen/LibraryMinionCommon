package me.ialistannen.libraryminioncommon.fetching.amazon

import me.ialistannen.libraryminioncommon.fetching.io.WebsiteFetcher
import me.ialistannen.libraryminioncommon.isbn.Isbn
import org.jsoup.nodes.Document

/**
 * Finds the detail page HTML for a book on amazon.
 */
internal object DetailPageFinder {

    private val BASE_URL = "https://www.amazon.de/s/&url=search-alias%%3Dstripbooks&field-keywords=%s"

    /**
     * Finds the html for the book's detail page.
     *
     * @param isbn the isbn of the book
     * @return the fetched document, if any
     */
    fun findDetailPage(isbn: Isbn): Document? {
        return fetchSearchPage(isbn).findDetailPage()
    }

    private fun fetchSearchPage(isbn: Isbn): Document {
        val url = BASE_URL.format(isbn.toString())

        return WebsiteFetcher.fetchPage(url)
    }

    private fun Document.findDetailPage(): Document? {
        val resultList = getElementById("s-results-list-atf") ?: return null

        val firstHit = resultList.getElementsByTag("li").firstOrNull() ?: return null

        val detailLink = firstHit.getElementsByClass("s-access-detail-page")
                .firstOrNull() ?: return null

        return WebsiteFetcher.fetchPage(detailLink.absUrl("href"))
    }
}