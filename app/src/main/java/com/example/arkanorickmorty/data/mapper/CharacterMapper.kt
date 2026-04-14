package com.example.arkanorickmorty.data.mapper

import com.example.arkanorickmorty.data.remote.response.CharacterDto
import com.example.arkanorickmorty.domain.models.CharacterModel
import com.example.arkanorickmorty.domain.models.CharacterStatus

fun CharacterDto.toDomain(): CharacterModel = CharacterModel(
    id = id,
    name = name,
    status = CharacterStatus.fromString(status),
    species = species,
    gender = gender,
    imageUrl = image,
    locationName = location.name,
    originName = origin.name,
    episodeCount = episode.size
)