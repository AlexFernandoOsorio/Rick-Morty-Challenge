package com.example.arkanorickmorty.domain.usecases

import com.example.arkanorickmorty.domain.models.CharacterModel
import com.example.arkanorickmorty.domain.models.CharacterStatus
import com.example.arkanorickmorty.domain.repository.CharacterRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCharactersUseCaseTest {

    private val repository: CharacterRepository = mockk()
    private lateinit var getCharactersUseCase: GetCharactersUseCase

    private val fakeCharacters = listOf(
        CharacterModel(1, "Rick", CharacterStatus.ALIVE, "Human", "", "", "Earth", "", episodeCount = 1),
        CharacterModel(2, "Morty", CharacterStatus.ALIVE, "Human", "", "", "Earth", "", episodeCount = 1),
    )

    @Before
    fun setUp() {
        getCharactersUseCase = GetCharactersUseCase(repository)
    }

    @Test
    fun `cuando el UseCase es invocado, debe retornar la lista de personajes del repositorio`() = runTest {
        // Given
        val page = 1
        coEvery { repository.getCharacters(page) } returns flowOf(fakeCharacters)

        // When
        val result = getCharactersUseCase(page).first()

        // Then
        assertEquals(fakeCharacters, result)
        assertEquals(fakeCharacters[0].name, result[0].name)
        coVerify(exactly = 1) { repository.getCharacters(page) }
    }

    @Test
    fun `cuando el repositorio devuelve una lista vacia, el UseCase debe emitir esa lista vacia`() = runTest {
        // Given
        val page = 5
        coEvery { repository.getCharacters(page) } returns flowOf(emptyList())

        // When
        val result = getCharactersUseCase(page).first()

        // Then
        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getCharacters(page) }
    }
}