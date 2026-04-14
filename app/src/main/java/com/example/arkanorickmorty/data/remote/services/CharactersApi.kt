package com.example.arkanorickmorty.data.remote.services

import com.example.arkanorickmorty.data.remote.response.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersApi {

    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int = 1): CharacterResponse
}