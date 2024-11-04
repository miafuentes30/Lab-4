package com.uvg.lab12mia.ktor

import com.uvg.lab12mia.Location
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)

@Serializable
data class LocationListDto(
    val results: List<LocationDto>
)

fun LocationDto.mapToLocationModel(): Location {
    return Location(
        id = id,
        name = name,
        type = type,
        dimension = dimension
    )
}
