package me.ialistannen.libraryminioncommon.fetching.goodreads

import me.ialistannen.libraryminioncommon.book.BookInformation
import me.ialistannen.libraryminioncommon.isbn.Isbn
import me.ialistannen.libraryminioncommon.isbn.IsbnParser
import org.jsoup.nodes.Document

/**
 * Extracts data about the book from the detail page.
 */
object DetailPageExtractor {

    fun extractFromDetailPage(detailPage: Document): BookInformation? {
        return try {
            BookInformation(
                    title = extractTitle(detailPage),
                    isbn = extractIsbn(detailPage)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun extractTitle(detailPage: Document): String =
            detailPage.getElementById("bookTitle").text()

    private fun extractIsbn(detailPage: Document): Isbn {
        val isbnString = detailPage.getElementsByAttributeValue("itemprop", "isbn").text()
        return IsbnParser.parseIsbn(isbnString)
                ?: throw IllegalArgumentException("Isbn not valid: '$isbnString'")
    }
}