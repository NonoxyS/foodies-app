package com.nonoxy.foodies.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nonoxy.foodies.app.ui.theme.FoodiesAppTheme
import com.nonoxy.foodies_main.cart.CartScreen
import com.nonoxy.foodies_main.cart.CartViewModel
import com.nonoxy.foodies_main.models.ProductUI
import com.nonoxy.foodies_main.productdetail.ProductDetailScreen
import com.nonoxy.foodies_main.products.ProductsScreen
import com.nonoxy.foodies_main.products.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodiesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val cartViewModel = hiltViewModel<CartViewModel>()
                    val cartState by cartViewModel.state.collectAsState()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Products.route
                    ) {
                        composable(route = Screen.Products.route) {
                            val productsViewModel = hiltViewModel<ProductsViewModel>()
                            val productsState by productsViewModel.state.collectAsState()

                            ProductsScreen(
                                productsState = productsState,
                                productsEvent = productsViewModel::onEvent,
                                cartState = cartState,
                                cartEvent = cartViewModel::onEvent,
                                navigateToDetail = { product ->
                                    navigateToDetail(
                                        navController = navController,
                                        product = product
                                    )
                                },
                                navigateToCart = { navController.navigate(route = Screen.Cart.route) { launchSingleTop } }
                            )
                        }

                        composable(route = Screen.ProductDetail.route) {
                            navController.previousBackStackEntry?.savedStateHandle?.get<ProductUI>("product")
                                ?.let { product ->
                                    ProductDetailScreen(
                                        product = product,
                                        cartState = cartState,
                                        event = cartViewModel::onEvent,
                                        navigateUp = { navController.navigateUp() }
                                    )
                                }
                        }

                        composable(route = Screen.Cart.route) {
                            CartScreen(
                                state = cartState,
                                event = cartViewModel::onEvent,
                                navigateUp = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun navigateToDetail(navController: NavController, product: ProductUI) {
    navController.currentBackStackEntry?.savedStateHandle?.set("product", product)
    navController.navigate(route = Screen.ProductDetail.route) { launchSingleTop }
}

sealed class Screen(val route: String) {
    object Products : Screen("productsScreen")
    object ProductDetail : Screen("productDetailScreen")
    object Cart : Screen("cartScreen")
}
