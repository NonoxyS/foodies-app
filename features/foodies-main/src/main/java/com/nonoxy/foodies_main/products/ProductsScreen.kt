package com.nonoxy.foodies_main.products

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nonoxy.foodies.main.R
import com.nonoxy.foodies_main.cart.CartEvent
import com.nonoxy.foodies_main.cart.CartState
import com.nonoxy.foodies_main.models.ProductUI
import com.nonoxy.foodies_main.products.views.ProductsCategoryRow
import com.nonoxy.foodies_main.products.views.ProductsList
import com.nonoxy.foodies_main.products.views.ProductsTopBar
import kotlinx.coroutines.launch


@Composable
fun ProductsScreen(
    productsState: ProductsScreenState,
    productsEvent: (ProductsEvent) -> Unit,
    cartState: CartState,
    cartEvent: (CartEvent) -> Unit,
    navigateToDetail: (ProductUI) -> Unit,
    navigateToCart: () -> Unit,
) {

    when (productsState) {
        is ProductsScreenState.Success -> {
            Products(
                state = productsState,
                cartState = cartState,
                cartEvent = cartEvent,
                event = productsEvent,
                navigateToDetail = navigateToDetail,
                navigateToCart = navigateToCart
            )
        }

        is ProductsScreenState.Error -> Log.d("Foodies", "$productsState")
        is ProductsScreenState.Loading -> Log.d("Foodies", "$productsState")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Products(
    state: ProductsScreenState.Success,
    cartState: CartState,
    cartEvent: (CartEvent) -> Unit,
    event: (ProductsEvent) -> Unit,
    navigateToDetail: (ProductUI) -> Unit,
    navigateToCart: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            ProductsTopBar(
                searchText = state.searchText,
                onSearchTextChanged = { event(ProductsEvent.SearchTextChanged(it)) },
                onFilterClick = { event(ProductsEvent.ToggleFilter) }
            )
            ProductsCategoryRow(
                categories = state.categories,
                selectedCategoryId = state.selectedCategoryId,
                onCategorySelected = { event(ProductsEvent.CategorySelected(it)) }
            )
            BottomShadow(height = 8.dp)

            ProductsList(
                products = state.filteredProducts,
                tags = state.tags,
                cartState = cartState,
                cartEvent = cartEvent,
                navigateToDetail = navigateToDetail
            )
        }

        AnimatedVisibility(
            visible = cartState.products.isNotEmpty(),
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(
                initialOffsetY = { -40 }
            ) + expandVertically(
                expandFrom = Alignment.Top
            ),
            exit = slideOutVertically()
        ) {
            TextButton(
                modifier = Modifier
                    .padding(16.dp)
                    .height(48.dp)
                    .fillMaxWidth(),
                onClick = navigateToCart,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cart),
                    contentDescription = null
                )
                Text(
                    text = "${cartState.totalPrice / 100} ₽",
                    style = MaterialTheme.typography.titleMedium.copy(lineHeight = 16.sp)
                )
            }
        }
    }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (state.isFilterOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    event(ProductsEvent.ToggleFilter)
                    sheetState.hide()
                }
            },
        ) {
            Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 32.dp)) {
                Text(
                    text = "Подобрать блюда",
                    style = MaterialTheme.typography.titleLarge.copy(letterSpacing = 0.15.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                FilterItem(
                    name = "Без мяса",
                    isSelected = state.selectedFilters["Без мяса"] ?: false,
                    onFilterSelected = { filterName, isSelected ->
                        event(ProductsEvent.UpdateSelectedFilters(filterName, isSelected))
                    }
                )
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
                FilterItem(
                    name = "Острое",
                    isSelected = state.selectedFilters["Острое"] ?: false,
                    onFilterSelected = { filterName, isSelected ->
                        event(ProductsEvent.UpdateSelectedFilters(filterName, isSelected))
                    }
                )
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
                FilterItem(
                    name = "Со скидкой",
                    isSelected = state.selectedFilters["Со скидкой"] ?: false,
                    onFilterSelected = { filterName, isSelected ->
                        event(ProductsEvent.UpdateSelectedFilters(filterName, isSelected))
                    }
                )

                TextButton(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            event(ProductsEvent.ApplySelectedFilters)
                            sheetState.hide()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text(
                        text = stringResource(R.string.done),
                        style = MaterialTheme.typography.titleMedium.copy(lineHeight = 16.sp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun FilterItem(
    name: String,
    isSelected: Boolean,
    onFilterSelected: (String, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .height(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onFilterSelected(name, !it) },
            modifier = Modifier.scale(1.2f),  // 20 * 1.2 = 24 dp
            colors = CheckboxDefaults.colors().copy(uncheckedBorderColor = Color.Gray)
        )
    }
}


@Composable
fun BottomShadow(alpha: Float = 0.1f, height: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = alpha),
                        Color.Transparent,
                    )
                )
            )
    )
}