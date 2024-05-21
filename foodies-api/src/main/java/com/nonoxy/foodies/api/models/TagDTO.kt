package com.nonoxy.foodies.api.models

import kotlinx.serialization.Serializable

@Serializable
data class TagDTO(
    val id: Long,
    val name: String
)
