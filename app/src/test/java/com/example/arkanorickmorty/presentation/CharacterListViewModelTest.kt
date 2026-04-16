package com.example.arkanorickmorty.presentation

import com.example.arkanorickmorty.core.CharacterListState
import com.example.arkanorickmorty.domain.models.CharacterModel
import com.example.arkanorickmorty.domain.models.CharacterStatus
import com.example.arkanorickmorty.domain.usecases.GetCharactersUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest {

    private val getCharactersUseCase: GetCharactersUseCase = mockk()
    private lateinit var viewModel: CharacterListViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val fakeCharacters = listOf(
        CharacterModel(1, "Rick", CharacterStatus.ALIVE, "Human", "", "", "Earth", "", episodeCount = 1),
        CharacterModel(2, "Morty", CharacterStatus.ALIVE, "Human", "", "", "Earth", "", episodeCount = 1),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init debe cargar personajes exitosamente`() = runTest {
        // Given
        coEvery { getCharactersUseCase(any()) } returns flowOf(fakeCharacters)

        // When
        viewModel = CharacterListViewModel(getCharactersUseCase)

        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue("El estado debería ser Success", state is CharacterListState.Success)
        assertEquals(fakeCharacters, (state as CharacterListState.Success).characters)
    }

    @Test
    fun `init debe mostrar error cuando falla la descarga`() = runTest {
        // Given
        val errorMessage = "No internet connection"
        coEvery { getCharactersUseCase(any()) } returns flow {
            throw Exception(errorMessage)
        }

        // When
        viewModel = CharacterListViewModel(getCharactersUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue("El estado debería ser Error", state is CharacterListState.Error)
        assertEquals(errorMessage, (state as CharacterListState.Error).message)
    }

    @Test
    fun `refreshWithRandomPage debe poner isRefreshing en false al terminar`() = runTest {
        // Given
        coEvery { getCharactersUseCase(any()) } returns flowOf(fakeCharacters)
        viewModel = CharacterListViewModel(getCharactersUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.refreshWithRandomPage()

        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(false, viewModel.isRefreshing.value)
        assertTrue(viewModel.uiState.value is CharacterListState.Success)
    }

    @Test
    fun `retry debe volver a intentar la carga tras un error`() = runTest {
        // Given
        coEvery { getCharactersUseCase(any()) } returns flow { throw Exception("Fail") }
        viewModel = CharacterListViewModel(getCharactersUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value is CharacterListState.Error)

        // Given
        coEvery { getCharactersUseCase(any()) } returns flowOf(fakeCharacters)

        // When
        viewModel.retry()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is CharacterListState.Success)
        assertEquals(fakeCharacters, (viewModel.uiState.value as CharacterListState.Success).characters)
    }
}