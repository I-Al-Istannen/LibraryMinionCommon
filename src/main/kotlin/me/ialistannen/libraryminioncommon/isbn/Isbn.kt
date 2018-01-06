package me.ialistannen.libraryminioncommon.isbn

/**
 * Represents an ISBN.
 *
 * The passed isbn is not checked for validity except enforcing that it only consists of numbers
 * (and X).
 */
interface Isbn {

    val stringForm: String
    val type: IsbnType

    /**
     * Validates this Isbn.
     *
     * @return true if this isbn is valid
     */
    fun validate(): Boolean

    /**
     * @return the [stringForm]
     */
    operator fun component1() = stringForm

    /**
     * @return the [type]
     */
    operator fun component2() = type
}

/**
 * The type of the isbn.
 */
enum class IsbnType {
    ISBN_10,
    ISBN_13
}