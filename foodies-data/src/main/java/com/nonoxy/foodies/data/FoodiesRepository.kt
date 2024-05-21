package com.nonoxy.foodies.data

import android.util.Log
import com.nonoxy.foodies.data.model.Category
import com.nonoxy.foodies.data.model.Product
import com.nonoxy.foodies.data.model.Tag
import com.nonoxy.foodies.api.FoodiesApi
import com.nonoxy.foodies.api.models.CategoryDTO
import com.nonoxy.foodies.api.models.ProductDTO
import com.nonoxy.foodies.api.models.TagDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class FoodiesRepository @Inject constructor(
    private val api: FoodiesApi,
) {

    fun getAllData(): Flow<RequestResult<Triple<List<Category>, List<Product>, List<Tag>>>> {

        return combine(
            getCategoriesFromServer(),
            getProductsFromServer(),
            getTagsFromServer()
        ) { categoriesResult, productsResult, tagsResult ->
            when {
                categoriesResult is RequestResult.Success &&
                productsResult is RequestResult.Success &&
                tagsResult is RequestResult.Success -> {
                    RequestResult.Success(
                        Triple(
                            categoriesResult.data.map { it.toCategory() },
                            productsResult.data.map { it.toProduct() },
                            tagsResult.data.map { it.toTag() })
                    )
                }
                categoriesResult is RequestResult.Error ||
                productsResult is RequestResult.Error ||
                tagsResult is RequestResult.Error -> {
                    RequestResult.Error()
                }
                else -> RequestResult.InProgress()
            }
        }

    }

    private fun getCategoriesFromServer(): Flow<RequestResult<List<CategoryDTO>>> {
        val apiRequest = flow { emit(api.getCategories()) }.flowOn(Dispatchers.IO)
            .onEach { result ->
                if (!result.isSuccess) {
                    Log.e("FoodiesRepository", "Error getting from server. Cause = ${result.exceptionOrNull()}")
                }
            }.map { it.toRequestResult() }

        val start = flowOf<RequestResult<List<CategoryDTO>>>(RequestResult.InProgress())

        return merge(apiRequest, start)
    }

    private fun getProductsFromServer(): Flow<RequestResult<List<ProductDTO>>> {
        val apiRequest = flow { emit(api.getProducts()) }.flowOn(Dispatchers.IO)
            .onEach { result ->
                if (!result.isSuccess) {
                    Log.e("FoodiesRepository", "Error getting from server. Cause = ${result.exceptionOrNull()}")
                }
            }.map { it.toRequestResult() }

        val start = flowOf<RequestResult<List<ProductDTO>>>(RequestResult.InProgress())

        return merge(apiRequest, start)
    }

    private fun getTagsFromServer(): Flow<RequestResult<List<TagDTO>>> {
        val apiRequest = flow { emit(api.getTags()) }.flowOn(Dispatchers.IO)
            .onEach { result ->
                if (!result.isSuccess) {
                    Log.e("FoodiesRepository", "Error getting from server. Cause = ${result.exceptionOrNull()}")
                }
            }.map { it.toRequestResult() }

        val start = flowOf<RequestResult<List<TagDTO>>>(RequestResult.InProgress())

        return merge(apiRequest, start)
    }
}
