package com.example.arkanorickmorty.data.repository

import com.example.arkanorickmorty.data.remote.response.CharacterDto
import com.example.arkanorickmorty.data.remote.response.CharacterResponse
import com.example.arkanorickmorty.data.remote.response.LocationDto
import com.example.arkanorickmorty.data.remote.response.PageInfoDto
import com.example.arkanorickmorty.data.remote.services.CharactersApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharacterRepositoryImplTest {

    private val api: CharactersApi = mockk()
    private lateinit var repository: CharacterRepositoryImpl

    @Before
    fun setUp() {
        repository = CharacterRepositoryImpl(api)
    }

    @Test
    fun `getCharacters debe llamar a la api y mapear los resultados a dominio`() = runTest {
        // Given
        val page = 1
        val fakeApiResult = CharacterResponse(
            info = PageInfoDto(count = 1, pages = 1, next = null, prev = null),
            results = listOf(CharacterDto(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = LocationDto(name = "Earth", url = ""),
                location = LocationDto(name = "Earth", url = ""),
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                episode = listOf("1"),
                url = "https://rickandmortyapi.com/api/character/1",
                created = "2017-11-04T18:48:46.250Z"
            ))
        )

        coEvery { api.getCharacters(page) } returns fakeApiResult

        // When
        val result = repository.getCharacters(page).first()

        // Then
        assertEquals(1, result.size)
        assertEquals("Rick Sanchez", result[0].name)
        assertEquals("Human", result[0].species)
    }
}