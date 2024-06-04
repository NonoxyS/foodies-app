package com.nonoxy.foodies_main.models

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize


@Parcelize
@Stable
data class ProductUI(
    val id: Long,
    val categoryID: Long,
    val name: String,
    val description: String,
    val image: String,
    val priceCurrent: Long,
    val priceOld: Long? = null,
    val measure: Int,
    val measureUnit: MeasureUnitUI,
    val energyPer100Grams: Float,
    val proteinsPer100Grams: Float,
    val fatsPer100Grams: Float,
    val carbohydratesPer100Grams: Float,
    val tagIDS: List<Long>
) : Parcelable

enum class MeasureUnitUI(val value: String) {
    GR("г")
}
