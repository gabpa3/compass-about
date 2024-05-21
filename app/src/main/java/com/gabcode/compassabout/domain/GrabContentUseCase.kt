package com.gabcode.compassabout.domain

import com.gabcode.compassabout.data.IAboutRepository

/**
 * UseCase to grab content and save it to local storage. Its a bypass to repository.
 *
 * @property repository [IAboutRepository] to fetch content.
 */
class GrabContentUseCase(private val repository: IAboutRepository) {
    suspend fun invoke() = repository.fetchContent()
}
