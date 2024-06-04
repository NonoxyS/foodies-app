package com.nonoxy.foodies_main.products

import com.nonoxy.foodies_main.models.ProductUI

sealed class ProductsEvent {
    class CategorySelected(val id: Long) : ProductsEvent()
    class SearchTextChanged(val newText: String) : ProductsEvent()
    class UpdateSelectedFilters(val filter: String, val isSelected: Boolean) : ProductsEvent()
    data object ApplySelectedFilters : ProductsEvent()
    class AddProductToCart(val product: ProductUI) : ProductsEvent()
    class DeleteProductFromCart(val product: ProductUI) : ProductsEvent()
    class UpProductCountInCart(val product: ProductUI) : ProductsEvent()
    class DownProductCountInCart(val product: ProductUI) : ProductsEvent()
    data object RefetchData : ProductsEvent()
}