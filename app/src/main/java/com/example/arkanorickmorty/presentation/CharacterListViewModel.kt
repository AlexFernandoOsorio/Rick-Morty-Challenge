package com.example.arkanorickmorty.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arkanorickmorty.core.CharacterListState
import com.example.arkanorickmorty.domain.usecases.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CharacterListState>(CharacterListState.Loading)
    val uiState: StateFlow<CharacterListState> = _uiState.asStateFlow()

    private var currentPage = 1

    init {
        loadCharacters()
    }

    fun loadCharacters(page: Int = 1) {
        currentPage = page
        viewModelScope.launch {
            _uiState.value = CharacterListState.Loading
            getCharactersUseCase()
                .catch { throwable ->
                    _uiState.value = CharacterListState.Error(
                        throwable.message ?: "Unknown error occurred"
                    )
                }
                .collect { characters ->
                    _uiState.value = CharacterListState.Success(characters)
                }
        }
    }

    fun retry() = loadCharacters(currentPage)
}