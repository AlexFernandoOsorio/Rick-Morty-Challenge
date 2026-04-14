package com.example.arkanorickmorty.data.repository

import com.example.arkanorickmorty.data.mapper.toDomain
import com.example.arkanorickmorty.data.remote.services.CharactersApi
import com.example.arkanorickmorty.domain.models.CharacterModel
import com.example.arkanorickmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: CharactersApi
) : CharacterRepository {

    override fun getCharacters(page: Int): Flow<List<CharacterModel>> = flow {
        val response = api.getCharacters(page)
        emit(response.results.map { it.toDomain() })
    }
}