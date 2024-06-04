package com.nonoxy.foodies_main.cart

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
class CartViewModel @Inject constructor(
    private val eventBusController: EventBusController
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
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

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.DeleteProduct -> {
                viewModelScope.launch(Dispatchers.Default) {
                    val updatedProductsInCart = state.value.products
                    updatedProductsInCart.remove(event.product)

                    withContext(Dispatchers.Main) {
                        eventBusController.publishEvent(
                            EventBus.ChangeProductsInCart(products = updatedProductsInCart)
                        )
                    }
                }
            }

            is CartEvent.DownProductCount -> {
                viewModelScope.launch(Dispatchers.Default) {
                    val updatedProductsInCart = state.value.products
                    if ((state.value.products[event.product] ?: 0) - 1 > 0) {
                        updatedProductsInCart[event.product] =
                            (state.value.products[event.product] ?: 0) - 1
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

            is CartEvent.UpProductCount -> {
                viewModelScope.launch(Dispatchers.Default) {
                    val updatedProductsInCart = state.value.products
                    updatedProductsInCart[event.product] =
                        (state.value.products[event.product] ?: 0) + 1

                    withContext(Dispatchers.Main) {
                        eventBusController.publishEvent(
                            EventBus.ChangeProductsInCart(products = updatedProductsInCart)
                        )
                    }
                }
            }
        }
    }

    private fun changeProductsInCart(products: MutableMap<ProductUI, Int>) {
        viewModelScope.launch(Dispatchers.Default) {
            val updatedTotalPrice = products.map { (product, countInCart) ->
                product.priceCurrent * countInCart
            }.sum()

            withContext(Dispatchers.Main) {
                _state.update { currentState ->
                    currentState.copy(
                        products = products,
                        totalPrice = updatedTotalPrice
                    )
                }
            }
        }
    }
}

data class CartState(
    val products: MutableMap<ProductUI, Int> = mutableStateMapOf(),
    val totalPrice: Long = 0
)
