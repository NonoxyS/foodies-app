package com.nonoxy.foodies_main.productdetail

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nonoxy.foodies_main.eventbus.EventBus
import com.nonoxy.foodies_main.eventbus.EventBusController
import com.nonoxy.foodies_main.models.ProductUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val eventBusController: EventBusController
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            eventBusController.eventBus.collect { event ->
                onEventBus(event)
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

    fun onEvent(event: ProductDetailEvent) {
        when (event) {
            is ProductDetailEvent.SelectProductDetail -> {
                selectProduct(event.product)
            }

            is ProductDetailEvent.AddProductToCart -> {
                viewModelScope.launch(Dispatchers.Default) {
                    val updatedProductsInCart = state.value.productsInCart
                    updatedProductsInCart[event.product] = 1

                    withContext(Dispatchers.Main) {
                        eventBusController.publishEvent(
                            EventBus.ChangeProductsInCart(products = updatedProductsInCart)
                        )
                    }
                }
            }

            is ProductDetailEvent.DeleteProductFromCart -> {
                viewModelScope.launch(Dispatchers.Default) {
                    val updatedProductsInCart = state.value.productsInCart
                    updatedProductsInCart.remove(event.product)

                    withContext(Dispatchers.Main) {
                        eventBusController.publishEvent(
                            EventBus.ChangeProductsInCart(products = updatedProductsInCart)
                        )
                    }
                }
            }

            is ProductDetailEvent.DownProductCountInCart -> {
                viewModelScope.launch(Dispatchers.Default) {
                    val updatedProductsInCart = state.value.productsInCart
                    if ((state.value.productsInCart[event.product] ?: 0) - 1 > 0) {
                        updatedProductsInCart[event.product] =
                            (state.value.productsInCart[event.product] ?: 0) - 1
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

            is ProductDetailEvent.UpProductCountInCart -> {
                viewModelScope.launch(Dispatchers.Default) {
                    val updatedProductsInCart = state.value.productsInCart
                    updatedProductsInCart[event.product] =
                        (state.value.productsInCart[event.product] ?: 0) + 1

                    withContext(Dispatchers.Main) {
                        eventBusController.publishEvent(
                            EventBus.ChangeProductsInCart(products = updatedProductsInCart)
                        )
                    }
                }
            }
        }
    }

    private fun selectProduct(product: ProductUI) {
        _state.update { currentState ->
            currentState.copy(selectedProduct = product)
        }
    }

    private fun changeProductsInCart(products: MutableMap<ProductUI, Int>) {
        _state.update { currentState ->
            currentState.copy(productsInCart = products)
        }
    }
}

@Stable
data class ProductDetailState(
    val selectedProduct: ProductUI? = null,
    val productsInCart: MutableMap<ProductUI, Int> = mutableStateMapOf(),
)