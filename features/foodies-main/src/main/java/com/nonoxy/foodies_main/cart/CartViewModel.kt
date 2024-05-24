package com.nonoxy.foodies_main.cart

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.nonoxy.foodies_main.models.ProductUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.AddProduct -> addProduct(event.product)
            is CartEvent.DeleteProduct -> deleteProduct(event.product)
            is CartEvent.DownProductCount -> changeProductCount(event.product, -1)
            is CartEvent.UpProductCount -> changeProductCount(event.product, 1)
        }
    }

    private fun addProduct(product: ProductUI) {
        _state.update { currentState ->
            if (!currentState.products.contains(product)) {
                currentState.copy(
                    products = currentState.products.also { it[product] = 1 },
                    totalPrice = currentState.totalPrice + product.priceCurrent
                )
            } else currentState
        }
    }

    private fun deleteProduct(product: ProductUI) {
        _state.update { currentState ->
            val updatedProducts = currentState.products
            val updatedTotalPrice =
                currentState.totalPrice - (product.priceCurrent * currentState.products.getOrDefault(
                    product, 0))

            updatedProducts.remove(product)

            currentState.copy(
                products = updatedProducts,
                totalPrice = updatedTotalPrice
            )
        }
    }

    private fun changeProductCount(product: ProductUI, delta: Int) {
        _state.update { currentState ->
            val updatedProducts = currentState.products
            val newCount = (currentState.products[product] ?: 0) + delta

            if (newCount > 0) updatedProducts[product] = newCount
            else updatedProducts.remove(product)

            if (delta < 0) {
                currentState.copy(
                    products = updatedProducts,
                    totalPrice = currentState.totalPrice - product.priceCurrent
                )
            } else {
                currentState.copy(
                    products = updatedProducts,
                    totalPrice = currentState.totalPrice + product.priceCurrent
                )
            }
        }
    }
}

data class CartState(
    val products: MutableMap<ProductUI, Int> = mutableStateMapOf(),
    val totalPrice: Long = 0
)
