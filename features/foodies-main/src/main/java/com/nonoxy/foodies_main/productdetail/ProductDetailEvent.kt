package com.nonoxy.foodies_main.productdetail

import com.nonoxy.foodies_main.models.ProductUI

sealed class ProductDetailEvent {
    class SelectProductDetail(val product: ProductUI) : ProductDetailEvent()
    class AddProductToCart(val product: ProductUI) : ProductDetailEvent()
    class DeleteProductFromCart(val product: ProductUI) : ProductDetailEvent()
    class UpProductCountInCart(val product: ProductUI) : ProductDetailEvent()
    class DownProductCountInCart(val product: ProductUI) : ProductDetailEvent()
}