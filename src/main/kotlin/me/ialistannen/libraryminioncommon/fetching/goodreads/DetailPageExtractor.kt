package me.ialistannen.libraryminioncommon.fetching.goodreads

import me.ialistannen.libraryminioncommon.book.BookInformation
import me.ialistannen.libraryminioncommon.isbn.Isbn
import me.ialistannen.libraryminioncommon.isbn.IsbnParser
import org.jsoup.nodes.Document

/**
 * Extracts data about the book from the detail page.
 */
object DetailPageExtractor {

    private val PUBLISHER_EXTRACT_REGEX = Regex("by\\s*(.+?)\\s*\\(")

    fun extractFromDetailPage(detailPage: Document): BookInformation? {
        return try {
            BookInformation(
                    title = extractTitle(detailPage),
                    isbn = extractIsbn(detailPage),
                    rating = extractRating(detailPage),
                    pageCount = extractPageCount(detailPage),
                    description = extractDescription(detailPage),
                    language = extractLanguage(detailPage),
                    publisher = extractPublisher(detailPage),
                    coverType = extractCoverType(detailPage),
                    author = extractAuthors(detailPage)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun extractTitle(detailPage: Document): String =
            detailPage.getElementById("bookTitle").text()

    private fun extractIsbn(detailPage: Document): Isbn {
        val isbnString = (detailPage.getItemPropString("isbn")
                ?: throw IllegalArgumentException("Isbn element not found"))

        return IsbnParser.parseIsbn(isbnString)
                ?: throw IllegalArgumentException("Isbn not valid: '$isbnString'")
    }

    private fun extractRating(detailPage: Document): Double? {
        return detailPage.getItemPropString("ratingValue")
                ?.toDouble()
                ?.div(5)
    }

    private fun extractPageCount(detailPage: Document): Int? {
        return detailPage.getItemPropString("numberOfPages")
                ?.split(" ")
                ?.firstOrNull()
                ?.toInt()
    }

    private fun extractDescription(detailPage: Document): String? {
        val descriptionContainer = detailPage.getElementById("description") ?: return null

        val description = descriptionContainer
                .getElementsByTag("span")
                .getOrNull(1) ?: return null

        return description.text()
    }

    private fun extractLanguage(detailPage: Document): String? =
            detailPage.getItemPropString("inLanguage")

    private fun extractPublisher(detailPage: Document): String? {
        @Suppress("LoopToCallChain")
        for (element in detailPage.select("#details > div:nth-child(2)")) {
            val result = PUBLISHER_EXTRACT_REGEX.find(element.text()) ?: continue

            return result.groups[1]?.value
        }

        return null
    }

    private fun extractCoverType(detailPage: Document): String?
            = detailPage.getItemPropString("bookFormat")

    private fun extractAuthors(detailPage: Document): List<Pair<String, String>> {
        val result: MutableList<Pair<String, String>> = mutableListOf()

        var lastAuthorName: String? = null

        for (authorElement in detailPage.getElementsByClass("authorName")) {
            // found an author with a role (e.g. Illustrator)
            if (authorElement.hasClass("role")) {
                val role = authorElement.text()
                        .replace("(", "")
                        .replace(")", "")
                        .trim()
                result.add(lastAuthorName!! to role)
                lastAuthorName = null
                continue
            }

            // found an author without a role
            if (lastAuthorName != null) {
                result.add(lastAuthorName to "Autor")
            }
            lastAuthorName = authorElement.text()
        }

        // the last author had no role
        if (lastAuthorName != null) {
            result.add(lastAuthorName to "Autor")
        }

        return result
    }

    private fun Document.getItemPropString(key: String): String? {
        return getElementsByAttributeValue("itemProp", key)
                .firstOrNull()
                ?.text()
    }
}