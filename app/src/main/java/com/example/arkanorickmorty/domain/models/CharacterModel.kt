package com.example.arkanorickmorty.domain.models

data class CharacterModel (
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val gender: String,
    val imageUrl: String,
    val locationName: String,
    val originName: String,
    val episodeCount: Int
)

enum class CharacterStatus(val label: String) {
    ALIVE("Alive"),
    DEAD("Dead"),
    UNKNOWN("Unknown");

    companion object {
        fun fromString(value: String): CharacterStatus = when (value.lowercase()) {
            "alive" -> ALIVE
            "dead" -> DEAD
            else -> UNKNOWN
        }
    }
}