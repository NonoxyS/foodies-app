package com.nonoxy.foodies_main.cart

import com.nonoxy.foodies_main.models.ProductUI


sealed class CartEvent {
    class DeleteProduct(val product: ProductUI) : CartEvent()
    class UpProductCount(val product: ProductUI) : CartEvent()
    class DownProductCount(val product: ProductUI) : CartEvent()
}