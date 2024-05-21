package com.nonoxy.foodies.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CategoryDTO(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
)