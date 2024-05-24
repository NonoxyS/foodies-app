package com.nonoxy.foodies_main.productdetail

internal sealed class ProductDetailEvent {
    object AddToCartEvent : ProductDetailEvent()
}