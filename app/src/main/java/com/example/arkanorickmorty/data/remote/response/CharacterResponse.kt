package com.example.arkanorickmorty.data.remote.response

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("info") val info: PageInfoDto,
    @SerializedName("results") val results: List<CharacterDto>
)
