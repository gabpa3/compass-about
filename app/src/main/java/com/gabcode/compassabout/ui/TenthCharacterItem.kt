package com.gabcode.compassabout.ui

import com.gabcode.compassabout.domain.TenthCharacterDomain

data class TenthCharacterItem(val position: Int, val character: Char)

fun Collection<TenthCharacterDomain>.toUIModel(): List<TenthCharacterItem> {
    return map { TenthCharacterItem(it.position, it.character) }
}
