package com.nonoxy.foodies.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDTO(
    @SerialName("id") val id: Long,
    @SerialName("category_id") val categoryID: Long,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("image") val image: String,
    @SerialName("price_current") val priceCurrent: Long,
    @SerialName("price_old") val priceOld: Long? = null,
    @SerialName("measure") val measure: Int,
    @SerialName("measure_unit") val measureUnit: MeasureUnit,
    @SerialName("energy_per_100_grams") val energyPer100Grams: Float,
    @SerialName("proteins_per_100_grams") val proteinsPer100Grams: Float,
    @SerialName("fats_per_100_grams") val fatsPer100Grams: Float,
    @SerialName("carbohydrates_per_100_grams") val carbohydratesPer100Grams: Float,
    @SerialName("tag_ids") val tagIDS: List<Long>
)

@Serializable
enum class MeasureUnit(val value: String) {
    @SerialName("г") GR("г")
}