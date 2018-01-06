package me.ialistannen.libraryminioncommon.isbn

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test


/**
 * Some tests for the IsbnParser.
 */
internal class IsbnParserTest {

    @Test
    fun `valid isbn 13 are valid`() {
        assertNotNull(IsbnParser.parseIsbn("9780306406157"))
        assertNotNull(IsbnParser.parseIsbn("9780765318411"))
        assertNotNull(IsbnParser.parseIsbn("9780770430078"))
    }

    @Test
    fun `valid isbn 13 in a strange format are valid`() {
        assertNotNull(IsbnParser.parseIsbn("9dsds   7 . f,asfjasöjgp80safs ,306406157"))
        assertNotNull(IsbnParser.parseIsbn("9, 7, 8, 0, 7, 6, -5-3-1-8-4-1-1-"))
        assertNotNull(IsbnParser.parseIsbn("9d78d0d7d7d0dddf430,k07-8"))
    }

    @Test
    fun `too short isbn 13 are rejected`() {
        assertNull(IsbnParser.parseIsbn("12345678901"))
        assertNull(IsbnParser.parseIsbn("123456789012"))
    }

    @Test
    fun `invalid isbn 13 checksum is rejected`() {
        assertNull(IsbnParser.parseIsbn("9780306406158"))
        assertNull(IsbnParser.parseIsbn("9780765318410"))
        assertNull(IsbnParser.parseIsbn("9780770431078"))
    }

    @Test
    fun `valid isbn 10 are valid`() {
        assertNotNull(IsbnParser.parseIsbn("0316081051"))
        assertNotNull(IsbnParser.parseIsbn("0375507256"))
        assertNotNull(IsbnParser.parseIsbn("912115628X"))
    }

    @Test
    fun `valid isbn 10 in a strange format are valid`() {
        assertNotNull(IsbnParser.parseIsbn("0,3,1,6,0 ,8,1,0 ,5, 1"))
        assertNotNull(IsbnParser.parseIsbn("0-375-507-256-"))
        assertNotNull(IsbnParser.parseIsbn("91da ganmgaljgfa211ddsds5xxxx6sdss28X"))
    }

    @Test
    fun `too short isbn 10 are rejected`() {
        assertNull(IsbnParser.parseIsbn("123456789"))
        assertNull(IsbnParser.parseIsbn("12345678"))
    }

    @Test
    fun `invalid isbn 10 checksum is rejected`() {
        assertNull(IsbnParser.parseIsbn("0316081851"))
        assertNull(IsbnParser.parseIsbn("0375507156"))
        assertNull(IsbnParser.parseIsbn("912115328X"))
    }
}