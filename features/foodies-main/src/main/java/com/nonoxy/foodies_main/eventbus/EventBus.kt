package com.nonoxy.foodies_main.eventbus

import com.nonoxy.foodies_main.models.ProductUI

sealed class EventBus {
    class ChangeProductsInCart(val products: MutableMap<ProductUI, Int>) : EventBus()
}