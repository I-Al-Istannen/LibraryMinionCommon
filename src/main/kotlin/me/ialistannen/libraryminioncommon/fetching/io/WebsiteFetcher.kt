package me.ialistannen.libraryminioncommon.fetching.io

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Fetches a webpage.
 */
object WebsiteFetcher {

    /**
     * Fetches a webpage via JSoup.
     *
     * @param url the url of the web page
     * @return the parsed document
     * @throws java.io.IOException if an error happens fetching the website
     */
    @JvmStatic
    fun fetchPage(url: String): Document
            = Jsoup.connect(url).userAgent("Mozilla/5.0").get()
}