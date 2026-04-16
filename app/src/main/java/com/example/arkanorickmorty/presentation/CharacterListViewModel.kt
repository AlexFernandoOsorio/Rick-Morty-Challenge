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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var currentPage = (1..30).random()

    init {
        loadCharacters()
    }

    fun loadCharacters(page: Int = currentPage, isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                _isRefreshing.value = true
            } else {
                _uiState.value = CharacterListState.Loading
            }

            getCharactersUseCase(page)
                .catch { throwable ->
                    _uiState.value = CharacterListState.Error(
                        throwable.message ?: "Error desconocido"
                    )
                    _isRefreshing.value = false
                }
                .collect { characters ->
                    _uiState.value = CharacterListState.Success(characters)
                    _isRefreshing.value = false
                }
        }
    }

    fun retry() = loadCharacters(currentPage)

    fun refreshWithRandomPage() {
        currentPage = (1..30).random()
        loadCharacters(currentPage, isRefresh = true)
    }
}