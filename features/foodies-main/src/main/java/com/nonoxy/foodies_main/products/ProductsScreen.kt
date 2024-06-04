package com.nonoxy.foodies_main.products

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nonoxy.foodies.main.R
import com.nonoxy.foodies_main.models.CategoryUI
import com.nonoxy.foodies_main.models.MeasureUnitUI
import com.nonoxy.foodies_main.models.ProductUI
import com.nonoxy.foodies_main.products.views.ProductsCategoryRow
import com.nonoxy.foodies_main.products.views.ProductsList
import com.nonoxy.foodies_main.products.views.ProductsScreenError
import com.nonoxy.foodies_main.products.views.ProductsTopBar
import com.nonoxy.foodies_theme.ui.theme.FoodiesAppTheme
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.launch


@Composable
fun ProductsScreen(
    state: ProductsScreenState,
    event: (ProductsEvent) -> Unit,
    navigateToDetail: (ProductUI) -> Unit,
    navigateToCart: () -> Unit,
) {
    when (state) {
        is ProductsScreenState.Display -> {
            Products(
                state = state,
                event = event,
                navigateToDetail = navigateToDetail,
                navigateToCart = navigateToCart
            )
        }

        is ProductsScreenState.Error -> ProductsScreenError(event = event)
        is ProductsScreenState.Loading -> { /* TODO */ }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Products(
    state: ProductsScreenState.Display,
    event: (ProductsEvent) -> Unit,
    navigateToDetail: (ProductUI) -> Unit,
    navigateToCart: () -> Unit
) {
    var isFilterOpen by rememberSaveable { mutableStateOf(false) }

    Column {
        ProductsTopBar(
            searchText = state.searchText,
            onSearchTextChanged = { event(ProductsEvent.SearchTextChanged(it)) },
            onFilterClick = { isFilterOpen = !isFilterOpen }
        )

        ProductsCategoryRow(
            categories = state.categories.toImmutableList(),
            selectedCategoryId = state.selectedCategoryId,
            onCategorySelected = { event(ProductsEvent.CategorySelected(it)) }
        )

        if (state.filteredProducts.isNotEmpty()) {
            ProductsList(
                modifier = Modifier.weight(1f),
                products = state.filteredProducts.toImmutableList(),
                productsInCart = state.productsInCart.toImmutableMap(),
                tags = state.tags.toImmutableList(),
                event = event,
                navigateToDetail = navigateToDetail
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.nothing_was_found),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .6f),
                )
            }
        }

        AnimatedVisibility(
            visible = state.productsInCart.isNotEmpty(),
            enter = slideInVertically(
                initialOffsetY = { -40 }
            ) + expandVertically(
                expandFrom = Alignment.Top
            ),
            exit = shrinkVertically(shrinkTowards = Alignment.Bottom) + slideOutVertically(
                targetOffsetY = { it })
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
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_cart),
                    contentDescription = stringResource(id = R.string.toCart)
                )
                Text(
                    text = "${state.totalPriceInCart / 100} ₽",
                    style = MaterialTheme.typography.titleMedium.copy(lineHeight = 16.sp)
                )
            }
        }
    }


    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (isFilterOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = {
                scope.launch {
                    isFilterOpen = !isFilterOpen
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
                            isFilterOpen = !isFilterOpen
                            sheetState.hide()
                        }
                        event(ProductsEvent.ApplySelectedFilters)
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
fun ProductsScreenPreview() {
    FoodiesAppTheme {
        Surface {
            ProductsScreen(
                state = ProductsScreenState.Display(
                    categories = listOf(
                        CategoryUI(1, "Горячие блюда"),
                        CategoryUI(2, "Суши"),
                        CategoryUI(3, "Соусы"),
                        CategoryUI(4, "Детское меню"),
                    ),
                    products = listOf(),
                    filteredProducts = listOf(
                        ProductUI(
                            id = 123,
                            categoryID = 4124,
                            name = "Том Ям",
                            description = "Кокосовое молоко, кальмары, креветки, помидоры черри, грибы вешанки",
                            image = "someUrl",
                            priceCurrent = 72000,
                            priceOld = 92000,
                            measure = 400,
                            measureUnit = MeasureUnitUI.GR,
                            energyPer100Grams = 198.9f,
                            proteinsPer100Grams = 10f,
                            fatsPer100Grams = 8.5f,
                            carbohydratesPer100Grams = 19.7f,
                            tagIDS = listOf()
                        ),
                        ProductUI(
                            id = 1234,
                            categoryID = 4124,
                            name = "Том Ям",
                            description = "Кокосовое молоко, кальмары, креветки, помидоры черри, грибы вешанки",
                            image = "someUrl",
                            priceCurrent = 72000,
                            priceOld = 92000,
                            measure = 400,
                            measureUnit = MeasureUnitUI.GR,
                            energyPer100Grams = 198.9f,
                            proteinsPer100Grams = 10f,
                            fatsPer100Grams = 8.5f,
                            carbohydratesPer100Grams = 19.7f,
                            tagIDS = listOf()
                        ),
                    ),
                    tags = listOf(),
                    selectedCategoryId = 1
                ),
                event = {},
                navigateToDetail = {},
                navigateToCart = {}
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
fun ProductsScreenEmptyPreview() {
    FoodiesAppTheme {
        Surface {
            ProductsScreen(
                state = ProductsScreenState.Display(
                    categories = listOf(
                        CategoryUI(1, "Горячие блюда"),
                        CategoryUI(2, "Суши"),
                        CategoryUI(3, "Соусы"),
                        CategoryUI(4, "Детское меню"),
                    ),
                    products = listOf(),
                    filteredProducts = listOf(),
                    tags = listOf(),
                    selectedCategoryId = 1
                ),
                event = {},
                navigateToDetail = {},
                navigateToCart = {}
            )
        }
    }
}