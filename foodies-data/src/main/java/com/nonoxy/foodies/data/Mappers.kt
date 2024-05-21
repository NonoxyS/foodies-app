package com.nonoxy.foodies.data


import com.nonoxy.foodies.data.model.Category
import com.nonoxy.foodies.data.model.MeasureUnit
import com.nonoxy.foodies.data.model.Product
import com.nonoxy.foodies.data.model.Tag
import com.nonoxy.foodies.api.models.CategoryDTO
import com.nonoxy.foodies.api.models.MeasureUnit as MeasureUnitDTO
import com.nonoxy.foodies.api.models.ProductDTO
import com.nonoxy.foodies.api.models.TagDTO


internal fun CategoryDTO.toCategory(): Category {
    return Category(
        id = id,
        name = name,
    )
}

internal fun ProductDTO.toProduct(): Product {
    return Product(
        id = id,
        categoryID = categoryID,
        name = name,
        description = description,
        image = image,
        priceCurrent = priceCurrent,
        priceOld = priceOld,
        measure = measure,
        measureUnit = measureUnit.toMeasureUnit(),
        energyPer100Grams = energyPer100Grams,
        proteinsPer100Grams = proteinsPer100Grams,
        fatsPer100Grams = fatsPer100Grams,
        carbohydratesPer100Grams = carbohydratesPer100Grams,
        tagIDS = tagIDS,
    )
}

internal fun MeasureUnitDTO.toMeasureUnit(): MeasureUnit {
    return when (this) {
        MeasureUnitDTO.GR -> MeasureUnit.GR
    }
}

internal fun TagDTO.toTag(): Tag {
    return Tag(
        id = id,
        name = name,
    )
}
