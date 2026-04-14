package com.example.arkanorickmorty.domain.repository

import com.example.arkanorickmorty.domain.models.CharacterModel
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(page: Int): Flow<List<CharacterModel>>
}