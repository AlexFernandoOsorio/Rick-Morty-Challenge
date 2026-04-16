package com.example.arkanorickmorty.domain.usecases

import com.example.arkanorickmorty.domain.models.CharacterModel
import com.example.arkanorickmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(page: Int): Flow<List<CharacterModel>> {
        return repository.getCharacters(page)
    }
}