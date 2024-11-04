package com.uvg.lab12mia.ktor

interface ApiRickYMorty {
    suspend fun getCharacters(): Result<CharacterListDto, NetworkError>
    suspend fun getLocations(): Result<LocationListDto, NetworkError>
    suspend fun getCharacter(id: Int): Result<CharacterDto, NetworkError>
    suspend fun getLocation(id: Int): Result<LocationDto, NetworkError>
}