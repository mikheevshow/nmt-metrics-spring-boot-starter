package com.github.mikheevshow

import java.text.Normalizer
import java.text.Normalizer.Form.NFD

private val ASCII_REGEX = "[^\\p{ASCII}]".toRegex()
private val ALPHABETIC_NUMERIC_REGEX = "[^a-zA-Z0-9\\s]+".toRegex()
private val SPACE_REGEX = "\\s+".toRegex()

fun String.slugify(replacement: String = "-"): String {
    return Normalizer
        .normalize(this, NFD)
        .replace(ASCII_REGEX, "")
        .replace(ALPHABETIC_NUMERIC_REGEX, "").trim()
        .replace(SPACE_REGEX, replacement)
        .lowercase()
}