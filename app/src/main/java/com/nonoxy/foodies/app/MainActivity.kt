package com.nonoxy.foodies.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.nonoxy.foodies.app.navigation.SetupNavGraph
import com.nonoxy.foodies_main.products.ProductsScreenState
import com.nonoxy.foodies_main.products.ProductsViewModel
import com.nonoxy.foodies_theme.ui.theme.FoodiesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val productsViewModel: ProductsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                when (productsViewModel.state.value) {
                    is ProductsScreenState.Display -> false
                    is ProductsScreenState.Error -> false
                    is ProductsScreenState.Loading -> true
                }
            }
        }
        setContent {
            FoodiesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    SetupNavGraph(navController = navController, productsViewModel = productsViewModel)
                }
            }
        }
    }
}