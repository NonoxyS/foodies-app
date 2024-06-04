package com.nonoxy.foodies.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nonoxy.foodies_main.cart.CartScreen
import com.nonoxy.foodies_main.cart.CartViewModel
import com.nonoxy.foodies_main.models.ProductUI
import com.nonoxy.foodies_main.productdetail.ProductDetailEvent
import com.nonoxy.foodies_main.productdetail.ProductDetailScreen
import com.nonoxy.foodies_main.productdetail.ProductDetailViewModel
import com.nonoxy.foodies_main.products.ProductsScreen
import com.nonoxy.foodies_main.products.ProductsViewModel

@Composable
fun SetupNavGraph(navController: NavHostController, productsViewModel: ProductsViewModel) {
    NavHost(
        navController = navController,
        startDestination = Screen.Products.route
    ) {
        composable(route = Screen.Products.route) {
            val state by productsViewModel.state.collectAsStateWithLifecycle()
            ProductsScreen(
                state = state,
                event = productsViewModel::onEvent,
                navigateToDetail = { product ->
                    navigateToDetail(navController = navController, product = product)
                },
                navigateToCart = {
                    navController.navigate(route = Screen.Cart.route) { launchSingleTop }
                }
            )
        }

        composable(route = Screen.ProductDetail.route) {
            val viewModel = hiltViewModel<ProductDetailViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            navController.previousBackStackEntry?.savedStateHandle?.get<ProductUI>("product")
                ?.let { product ->
                    viewModel.onEvent(ProductDetailEvent.SelectProductDetail(product))
                    ProductDetailScreen(
                        product = product,
                        state = state,
                        event = viewModel::onEvent,
                        navigateUp = { navController.navigateUp() },
                        navigateToCart = { navController.navigate(Screen.Cart.route) }
                    )
                }
        }

        composable(route = Screen.Cart.route) {
            val viewModel = hiltViewModel<CartViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            CartScreen(
                state = state,
                event = viewModel::onEvent,
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

private fun navigateToDetail(navController: NavController, product: ProductUI) {
    navController.currentBackStackEntry?.savedStateHandle?.set("product", product)
    navController.navigate(route = Screen.ProductDetail.route) { launchSingleTop }
}