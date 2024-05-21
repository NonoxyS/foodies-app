package com.nonoxy.foodies_main.products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nonoxy.foodies.data.RequestResult
import com.nonoxy.foodies_main.GetFoodiesDataUseCase
import com.nonoxy.foodies_main.models.CategoryUI
import com.nonoxy.foodies_main.models.ProductUI
import com.nonoxy.foodies_main.models.TagUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProductsMainViewModel @Inject constructor(
    getFoodiesDataUseCase: GetFoodiesDataUseCase,
) : ViewModel() {

    /*val state: StateFlow<ProductsScreenState> = getFoodiesDataUseCase.invoke()
        .map { it.toProductsScreenState() }
        .stateIn(viewModelScope, SharingStarted.Lazily, ProductsScreenState.Loading)*/

    private val _state: MutableStateFlow<ProductsScreenState> =
        MutableStateFlow(ProductsScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getFoodiesDataUseCase.invoke()
                .map { it.toProductsScreenState() }
                .collect { newState ->
                    _state.update { newState }
                }
        }
    }

    fun updateSelectedCategoryId(id: Long) {
        _state.update { currentState ->
            if (currentState is ProductsScreenState.Success) {
                val newState = currentState.copy(selectedCategoryId = id)
                newState.copy(filteredProducts = filterProducts(newState))
            } else {
                currentState
            }
        }
    }

    fun updateSearchText(text: String) {
        _state.update { currentState ->
            if (currentState is ProductsScreenState.Success) {
                val newState = currentState.copy(searchText = text)
                newState.copy(filteredProducts = filterProducts(newState))
            } else {
                currentState
            }
        }
    }

    fun toggleFilterOpen() {
        _state.update { currentState ->
            if (currentState is ProductsScreenState.Success) {
                currentState.copy(isFilterOpen = !currentState.isFilterOpen)
            } else {
                currentState
            }
        }
    }

    fun onFilterDoneButton() {
        _state.update { currentState ->
            if (currentState is ProductsScreenState.Success) {
                val newState = currentState.copy(isFilterOpen = !currentState.isFilterOpen)
                newState.copy(filteredProducts = filterProducts(newState))
            } else {
                currentState
            }
        }
    }

    fun updateSelectedFilters(filter: String, isSelected: Boolean) {
        _state.update { currentState ->
            if (currentState is ProductsScreenState.Success) {
                currentState.copy(selectedFilters = currentState.selectedFilters.map {
                    if (it.key == filter) filter to !isSelected
                    else it.key to it.value
                }.toMap())
            } else {
                currentState
            }
        }
    }


    private fun filterProducts(state: ProductsScreenState.Success): List<ProductUI> {
        val filterIds = state.selectedFilters.filter { it.value }.map { filter ->
            when (filter.key) {
                "Без мяса" -> state.tags.find { it.name == "Вегетарианское блюдо" }?.id
                "Острое" -> state.tags.find { it.name == filter.key }?.id
                else -> 777
            }
        }
        return state.products.filter { product ->
            product.categoryID == state.selectedCategoryId &&
                    product.name.contains(state.searchText.trim(), ignoreCase = true) &&
                    filterIds.all {
                        if (it == 777L) product.priceOld != null
                        else product.tagIDS.contains(it)
                    }
        }
    }
}


private fun RequestResult<Triple<List<CategoryUI>, List<ProductUI>, List<TagUI>>>.toProductsScreenState(): ProductsScreenState {
    return when (this) {
        is RequestResult.Error -> ProductsScreenState.Error
        is RequestResult.InProgress -> ProductsScreenState.Loading
        is RequestResult.Success -> ProductsScreenState.Success(
            categories = data.first,
            products = data.second,
            filteredProducts = data.second.filter { product ->
                product.categoryID == data.first.first().id
            },
            tags = data.third,
            selectedCategoryId = data.first.first().id,
        )
    }
}

internal sealed class ProductsScreenState {

    data object Loading : ProductsScreenState()
    data object Error : ProductsScreenState()
    data class Success(
        val categories: List<CategoryUI>,
        val products: List<ProductUI>,
        val filteredProducts: List<ProductUI>,
        val tags: List<TagUI>,
        val selectedCategoryId: Long,
        val searchText: String = "",
        val isFilterOpen: Boolean = false,
        val selectedFilters: Map<String, Boolean> = mapOf(
            "Без мяса" to false,
            "Острое" to false,
            "Со скидкой" to false
        ),
    ) : ProductsScreenState()
}