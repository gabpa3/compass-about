package com.gabcode.compassabout.domain

import com.gabcode.compassabout.data.IAboutRepository

class GrabContentUseCase(private val repository: IAboutRepository) {
    suspend fun invoke() = repository.fetchContent()
}
