package com.example.arkanorickmorty.core

import com.example.arkanorickmorty.domain.models.CharacterModel

sealed class CharacterListState {
    object Loading : CharacterListState()
    data class Success(val characters: List<CharacterModel>) : CharacterListState()
    data class Error(val message: String) : CharacterListState()
}