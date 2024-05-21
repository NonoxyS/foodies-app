package com.nonoxy.foodies_main

import com.nonoxy.foodies.data.FoodiesRepository
import com.nonoxy.foodies.data.RequestResult
import com.nonoxy.foodies.data.map
import com.nonoxy.foodies.data.model.Category
import com.nonoxy.foodies.data.model.MeasureUnit
import com.nonoxy.foodies.data.model.Product
import com.nonoxy.foodies.data.model.Tag
import com.nonoxy.foodies_main.models.CategoryUI
import com.nonoxy.foodies_main.models.MeasureUnitUI
import com.nonoxy.foodies_main.models.ProductUI
import com.nonoxy.foodies_main.models.TagUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFoodiesDataUseCase @Inject constructor(
    private val repository: FoodiesRepository
) {

    internal operator fun invoke()
            : Flow<RequestResult<Triple<List<CategoryUI>, List<ProductUI>, List<TagUI>>>> {

        return repository.getAllData()
            .map { result ->
                result.map { (categories, products, tags) ->
                    Triple(
                        categories.map { it.toUiCategory() },
                        products.map { it.toProduct() },
                        tags.map { it.toTag() },
                    )
                }
            }

    }

    private fun Category.toUiCategory(): CategoryUI {
        return CategoryUI(
            id = id,
            name = name
        )
    }

    private fun Product.toProduct(): ProductUI {
        return ProductUI(
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

    private fun MeasureUnit.toMeasureUnit(): MeasureUnitUI {
        return when (this) {
            MeasureUnit.GR -> MeasureUnitUI.GR
        }
    }

    private fun Tag.toTag(): TagUI {
        return TagUI(
            id = id,
            name = name,
        )
    }
}
