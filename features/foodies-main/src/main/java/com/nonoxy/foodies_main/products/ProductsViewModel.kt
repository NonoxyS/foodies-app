package com.nonoxy.foodies_main.products

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nonoxy.foodies.data.RequestResult
import com.nonoxy.foodies_main.GetFoodiesDataUseCase
import com.nonoxy.foodies_main.eventbus.EventBus
import com.nonoxy.foodies_main.eventbus.EventBusController
import com.nonoxy.foodies_main.models.CategoryUI
import com.nonoxy.foodies_main.models.ProductUI
import com.nonoxy.foodies_main.models.TagUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getFoodiesDataUseCase: GetFoodiesDataUseCase,
    private val eventBusController: EventBusController,
) : ViewModel() {

    private val _state: MutableStateFlow<ProductsScreenState> =
        MutableStateFlow(ProductsScreenState.Loading)
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            getFoodiesDataUseCase.invoke()
                .map { it.toProductsScreenState() }
                .collect { newState ->
                    _state.update { newState }
                }

            eventBusController.eventBus.collect { event ->
                onEventBus(eventBus = event)
            }
        }
    }

    private fun onEventBus(eventBus: EventBus) {
        when (eventBus) {
            is EventBus.ChangeProductsInCart -> changeProductsInCart(
                products = eventBus.products
            )
        }
    }

    fun onEvent(event: ProductsEvent) {
        when (event) {
            is ProductsEvent.RefetchData -> fetchData()
            is ProductsEvent.ApplySelectedFilters -> applySelectedFilters()
            is ProductsEvent.CategorySelected -> updateSelectedCategoryId(event.id)
            is ProductsEvent.SearchTextChanged -> updateSearchText(event.newText)
            is ProductsEvent.UpdateSelectedFilters -> updateSelectedFilters(
                filter = event.filter,
                isSelected = event.isSelected
            )

            is ProductsEvent.AddProductToCart -> {
                viewModelScope.launch(Dispatchers.Default) {
                    val updatedProductsInCart =
                        (state.value as ProductsScreenState.Display).productsInCart
                    updatedProductsInCart[event.product] = 1

                    withContext(Dispatchers.Main) {
                        eventBusController.publishEvent(
                            EventBus.ChangeProductsInCart(products = updatedProductsInCart)
                        )
                    }
                }
            }

            is ProductsEvent.DeleteProductFromCart -> {
                viewModelScope.launch(Dispatchers.Default) {
                    val updatedProductsInCart =
                        (state.value as ProductsScreenState.Display).productsInCart
                    updatedProductsInCart.remove(event.product)

                    withContext(Dispatchers.Main) {
                        eventBusController.publishEvent(
                            EventBus.ChangeProductsInCart(products = updatedProductsInCart)
                        )
                    }
                }
            }

            is ProductsEvent.DownProductCountInCart -> {
                viewModelScope.launch(Dispatchers.Default) {
                    val updatedProductsInCart =
                        (state.value as ProductsScreenState.Display).productsInCart

                    if (((state.value as ProductsScreenState.Display).productsInCart[event.product] ?: 0) - 1 > 0) {
                        updatedProductsInCart[event.product] =
                            ((state.value as ProductsScreenState.Display).productsInCart[event.product] ?: 0) - 1
                    } else {
                        updatedProductsInCart.remove(event.product)
                    }

                    withContext(Dispatchers.Main) {
                        eventBusController.publishEvent(
                            EventBus.ChangeProductsInCart(products = updatedProductsInCart)
                        )
                    }
                }
            }

            is ProductsEvent.UpProductCountInCart -> {
                viewModelScope.launch(Dispatchers.Default) {
                    val updatedProductsInCart =
                        (state.value as ProductsScreenState.Display).productsInCart
                    updatedProductsInCart[event.product] =
                        ((state.value as ProductsScreenState.Display).productsInCart[event.product] ?: 0) + 1

                    withContext(Dispatchers.Main) {
                        eventBusController.publishEvent(
                            EventBus.ChangeProductsInCart(products = updatedProductsInCart)
                        )
                    }
                }
            }
        }
    }

    private fun updateSelectedCategoryId(id: Long) {
        viewModelScope.launch {
            _state.update { currentState ->
                if (currentState is ProductsScreenState.Display) {
                    val newState = currentState.copy(selectedCategoryId = id)
                    newState.copy(filteredProducts = filterProducts(newState))
                } else {
                    currentState
                }
            }
        }
    }

    private fun updateSearchText(text: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                if (currentState is ProductsScreenState.Display) {
                    currentState.copy(searchText = text)
                } else {
                    currentState
                }
            }
        }
        debounceSearch()
    }

    private fun debounceSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300L)
            _state.update { currentState ->
                if (currentState is ProductsScreenState.Display) {
                    currentState.copy(filteredProducts = filterProducts(currentState))
                } else currentState
            }
        }
    }

    private fun applySelectedFilters() {
        viewModelScope.launch {
            _state.update { currentState ->
                if (currentState is ProductsScreenState.Display) {
                    currentState.copy(filteredProducts = filterProducts(currentState))
                } else {
                    currentState
                }
            }
        }
    }

    private fun updateSelectedFilters(filter: String, isSelected: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            _state.update { currentState ->
                if (currentState is ProductsScreenState.Display) {
                    val updatedFilters = currentState.selectedFilters.map {
                        if (it.key == filter) filter to !isSelected
                        else it.key to it.value
                    }.toMap()

                    withContext(Dispatchers.Main) {
                        currentState.copy(selectedFilters = updatedFilters)
                    }
                } else {
                    currentState
                }
            }
        }
    }


    private suspend fun filterProducts(state: ProductsScreenState.Display): List<ProductUI> {
        val filteredProducts = viewModelScope.async(Dispatchers.Default) {
            val filterIds = state.selectedFilters.filter { it.value }.map { filter ->
                when (filter.key) {
                    "Без мяса" -> state.tags.find { it.name == "Вегетарианское блюдо" }?.id
                    "Острое" -> state.tags.find { it.name == filter.key }?.id
                    else -> 777
                }
            }
            state.products.filter { product ->
                product.categoryID == state.selectedCategoryId &&
                        product.name.contains(state.searchText.trim(), ignoreCase = true) &&
                        filterIds.all { tagId ->
                            if (tagId == 777L) product.priceOld != null // 777 - Костыль, т.к. в апи тегах нет тега "Со скидкой"
                            else product.tagIDS.contains(tagId)
                        }
            }
        }
        return filteredProducts.await()
    }

    private fun changeProductsInCart(products: MutableMap<ProductUI, Int>) {
        viewModelScope.launch(Dispatchers.Default) {
            val updatedTotalPrice = products.map { (product, countInCart) ->
                product.priceCurrent * countInCart
            }.sum()

            withContext(Dispatchers.Main) {
                _state.update { currentState ->
                    if (currentState is ProductsScreenState.Display) {
                        currentState.copy(
                            productsInCart = products,
                            totalPriceInCart = updatedTotalPrice
                        )
                    } else currentState
                }
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            getFoodiesDataUseCase.invoke()
                .map { it.toProductsScreenState() }
                .collect { newState ->
                    _state.update { newState }
                }
        }
    }


    private fun RequestResult<Triple<List<CategoryUI>, List<ProductUI>, List<TagUI>>>.toProductsScreenState(): ProductsScreenState {
        return when (this) {
            is RequestResult.Error -> ProductsScreenState.Error
            is RequestResult.InProgress -> ProductsScreenState.Loading
            is RequestResult.Success -> {
                val (categories, products, tags) = data

                ProductsScreenState.Display(
                    categories = categories,
                    products = products,
                    filteredProducts = products.filter { product ->
                        product.categoryID == categories.first().id
                    },
                    tags = tags,
                    selectedCategoryId = categories.first().id,
                )
            }
        }
    }
}

@Stable
sealed class ProductsScreenState {

    data object Loading : ProductsScreenState()
    data object Error : ProductsScreenState()

    @Stable
    data class Display(
        val categories: List<CategoryUI>,
        val products: List<ProductUI>,
        val filteredProducts: List<ProductUI>,
        val tags: List<TagUI>,
        val selectedCategoryId: Long,
        val searchText: String = "",
        val selectedFilters: Map<String, Boolean> = mapOf(
            "Без мяса" to false,
            "Острое" to false,
            "Со скидкой" to false
        ),
        val productsInCart: MutableMap<ProductUI, Int> = mutableStateMapOf(),
        val totalPriceInCart: Long = 0,
    ) : ProductsScreenState()
}