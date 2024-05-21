package com.nonoxy.foodies.data.model


data class Product(
    val id: Long,
    val categoryID: Long,
    val name: String,
    val description: String,
    val image: String,
    val priceCurrent: Long,
    val priceOld: Long? = null,
    val measure: Int,
    val measureUnit: MeasureUnit,
    val energyPer100Grams: Float,
    val proteinsPer100Grams: Float,
    val fatsPer100Grams: Float,
    val carbohydratesPer100Grams: Float,
    val tagIDS: List<Long>
)

enum class MeasureUnit(val value: String) {
    GR("г")
}
