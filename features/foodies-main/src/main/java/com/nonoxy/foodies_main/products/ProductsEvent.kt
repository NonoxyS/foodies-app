package com.nonoxy.foodies_main.products

sealed class ProductsEvent {
    class CategorySelected(val id: Long) : ProductsEvent()
    class SearchTextChanged(val newText: String) : ProductsEvent()
    object ToggleFilter : ProductsEvent()
    class UpdateSelectedFilters(val filter: String, val isSelected: Boolean) : ProductsEvent()
    object ApplySelectedFilters : ProductsEvent()
}