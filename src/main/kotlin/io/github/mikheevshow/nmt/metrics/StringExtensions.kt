/*

MIT License

Copyright (c) 2023 Ilya Mikheev

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

 */

package io.github.mikheevshow.nmt.metrics

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