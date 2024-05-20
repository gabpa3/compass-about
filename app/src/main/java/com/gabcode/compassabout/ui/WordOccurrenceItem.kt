package com.gabcode.compassabout.ui

import com.gabcode.compassabout.domain.WordOccurrenceDomain

data class WordOccurrenceItem(val word: String, val occurrences: Int)

fun Collection<WordOccurrenceDomain>.toUIModel(): List<WordOccurrenceItem> {
    return map { WordOccurrenceItem(it.word, it.occurrences) }
}
