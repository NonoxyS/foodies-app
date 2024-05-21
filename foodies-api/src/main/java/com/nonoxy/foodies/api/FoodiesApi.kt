package com.nonoxy.foodies.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.nonoxy.foodies.api.models.CategoryDTO
import com.nonoxy.foodies.api.models.ProductDTO
import com.nonoxy.foodies.api.models.TagDTO
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET


interface FoodiesApi {

    @GET("Categories.json")
    suspend fun getCategories(): Result<List<CategoryDTO>>

    @GET("Products.json")
    suspend fun getProducts(): Result<List<ProductDTO>>

    @GET("Tags.json")
    suspend fun getTags(): Result<List<TagDTO>>
}

fun FoodiesApi(
    baseUrl: String,
    okHttpClient: OkHttpClient? = null,
): FoodiesApi {
    return retrofit(baseUrl, okHttpClient).create()
}

private fun retrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient?,
): Retrofit {

    val jsonConverterFactory = Json.asConverterFactory("application/json".toMediaType())
    val modifiedOkHttpClient = (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .addConverterFactory(jsonConverterFactory)
        .client(modifiedOkHttpClient)
        .build()
}