package com.nonoxy.foodies.app.navigation

sealed class Screen(val route: String) {
    data object Products : Screen("productsScreen")
    data object ProductDetail : Screen("productDetailScreen")
    data object Cart : Screen("cartScreen")
}
