package me.ialistannen.libraryminioncommon.fetching.amazon

import me.ialistannen.libraryminioncommon.book.BookInformation
import me.ialistannen.libraryminioncommon.isbn.Isbn
import me.ialistannen.libraryminioncommon.isbn.IsbnParser
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * Extracts [BookInformation] from an amazon detail page.
 */
object DetailPageExtractor {

    private val PUBLISHER_EXTRACTION_REGEX = Regex("\\s*(.+)\\s*\\(")
    private val RATING_EXTRACTION_REGEX = Regex("(\\d\\.\\d)\\s*von.+Stern.+")
    private val URL_EXTRACTION_REGEX = Regex("\"(https://.+?)\"")
    private val PAGE_EXTRACTION_REGEX = Regex("(\\d+).+Seiten")

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
                    coverImageUrl = extractCoverImageUrl(detailPage),
                    author = extractAuthors(detailPage)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun extractAuthors(detailPage: Document): List<Pair<String, String>> {
        return emptyList()
    }

    private fun extractCoverImageUrl(detailPage: Document): String? {
        val imageContainer = detailPage.getElementById("main-image-container") ?: return null

        val urlsString = imageContainer.getElementsByTag("img")
                .firstOrNull()
                ?.attr("data-a-dynamic-image") ?: return null

        val matchResult = URL_EXTRACTION_REGEX.findAll(urlsString)
                .lastOrNull() ?: return null

        return matchResult.groups[1]?.value
    }

    private fun extractCoverType(detailPage: Document): String?
            = null

    private fun extractDescription(detailPage: Document): String? {
        val descriptionContainer = detailPage
                .getElementsByAttributeValue("data-feature-name", "bookDescription")
                .firstOrNull() ?: return null

        return descriptionContainer.getElementsByTag("noscript")?.text()
    }

    private fun extractIsbn(detailPage: Document): Isbn {
        val isbnString = detailPage.getContentInformationWithKey("ISBN-13:")
                ?: throw IllegalArgumentException("No isbn found in the detail page")
        return IsbnParser.parseIsbn(isbnString)
                ?: throw IllegalArgumentException("Isbn not valid: '$isbnString'")
    }

    private fun extractLanguage(detailPage: Document): String? =
            detailPage.getContentInformationWithKey("Sprache:")

    private fun extractPageCount(detailPage: Document): Int? {
        return detailPage.applyToContentInformation {
            mapNotNull {
                it.nextSibling()
            }
                    .mapNotNull {
                        PAGE_EXTRACTION_REGEX.find(it.toString())
                    }
                    .mapNotNull {
                        it.groups[1]?.value
                    }
        }
                .firstOrNull()
                ?.toIntOrNull()
    }

    private fun extractPublisher(detailPage: Document): String? {
        val publisher = detailPage.getContentInformationWithKey("Verlag:") ?: return null

        val matchResult = PUBLISHER_EXTRACTION_REGEX.find(publisher) ?: return null

        return matchResult.groups[1]?.value?.trim()
    }

    private fun extractRating(detailPage: Document): Double? {
        val ratingContainer = detailPage.getElementById("averageCustomerReviews") ?: return null
        @Suppress("LoopToCallChain")
        for (element in ratingContainer.getElementsByAttribute("title")) {
            val matchResult = RATING_EXTRACTION_REGEX.find(element.text()) ?: continue

            if (matchResult.groups[1] != null) {
                // X.X from 5 stars
                return matchResult.groups[1]!!.value.toDouble().div(5)
            }
        }
        return null
    }

    private fun extractTitle(detailPage: Document): String =
            detailPage.getElementById("productTitle").text()

    private fun Document.getContentInformationWithKey(key: String): String? {
        return applyToContentInformation {
            filter { it.text().trim() == key }
                    .map { it.nextSibling().toString().trim() }
        }.firstOrNull()
    }

    private fun <R> Document.applyToContentInformation(function: (List<Element>).() -> List<R>): List<R> {
        val elements = getElementById("detail_bullets_id")
                ?.getElementsByTag("li") ?: return emptyList()

        // Layout: "<b>Key</b>Value"
        val list = elements
                .filter { it.children().size == 1 }
                .map { it.child(0) }
        return function.invoke(list)
    }
}