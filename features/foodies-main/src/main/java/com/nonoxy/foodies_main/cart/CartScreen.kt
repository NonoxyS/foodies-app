package com.nonoxy.foodies_main.cart

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nonoxy.foodies.main.R
import com.nonoxy.foodies_main.models.MeasureUnitUI
import com.nonoxy.foodies_main.models.ProductUI
import com.nonoxy.foodies_theme.ui.theme.FoodiesAppTheme
import com.nonoxy.foodies_theme.ui.theme.common.FImage
import com.nonoxy.foodies_theme.ui.theme.common.SwipeToDeleteContainer
import com.nonoxy.foodies_theme.ui.theme.common.roundRectShadow
import com.nonoxy.foodies_theme.ui.theme.common.shadow
import com.nonoxy.foodies_theme.ui.theme.entity.CustomShadowParams

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    state: CartState,
    event: (CartEvent) -> Unit,
    navigateUp: () -> Unit
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.cart),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 32.dp)
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { navigateUp() },
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrowleft),
                        contentDescription = stringResource(id = R.string.back),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            modifier = Modifier
                .padding(bottom = 8.dp)
                .roundRectShadow(
                    customShadowParams = CustomShadowParams.shadow2(),
                    cornerRadius = 8.dp
                )
        )

        if (state.products.isNotEmpty()) {
            CartList(modifier = Modifier.weight(1f), state = state, event = event)
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.empty_cart),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .6f),
                )
            }
        }

        AnimatedVisibility(
            visible = state.products.isNotEmpty(),
            exit = shrinkVertically(shrinkTowards = Alignment.Bottom) + slideOutVertically(
                targetOffsetY = { it })
        ) {
            TextButton(
                modifier = Modifier
                    .padding(16.dp)
                    .height(48.dp)
                    .fillMaxWidth(),
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Заказать за ${state.totalPrice / 100} ₽",
                    style = MaterialTheme.typography.titleMedium.copy(lineHeight = 16.sp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CartList(
    modifier: Modifier = Modifier,
    state: CartState,
    event: (CartEvent) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items = state.products.toList(), key = { it.first.id }) { (product, productCount) ->
            Box(modifier = Modifier.animateItemPlacement()) {
                SwipeToDeleteContainer(
                    item = product,
                    animationDuration = 200,
                    onDelete = { event(CartEvent.DeleteProduct(it)) }
                ) {
                    CartItem(product = product, productCount = productCount, event = event)
                }
                HorizontalDivider(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .3f)
                )
            }
        }
    }
}

@Composable
private fun CartItem(
    product: ProductUI,
    productCount: Int,
    event: (CartEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxWidth()
            .height(130.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FImage(
            drawableResId = R.drawable.food,
            contentDescription = null,
            modifier = Modifier.size(96.dp)
        )
        Column(modifier = Modifier.height(96.dp)) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = { event(CartEvent.DownProductCount(product)) },
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(blurRadius = 16.dp, spread = (-10).dp, offsetY = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = if (productCount > 1) ImageVector.vectorResource(id = R.drawable.ic_minus)
                        else ImageVector.vectorResource(id = R.drawable.ic_delete),
                        contentDescription = stringResource(id = R.string.product_count_decrease),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "$productCount",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(32.dp)
                )

                Button(
                    onClick = { event(CartEvent.UpProductCount(product)) },
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(blurRadius = 16.dp, spread = (-10).dp, offsetY = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_plus),
                        contentDescription = stringResource(id = R.string.product_count_increase),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${product.priceCurrent / 100} ₽",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    if (product.priceOld != null) {
                        Text(
                            text = "${product.priceOld / 100} ₽",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .6f),
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun CartItemPreview() {
    FoodiesAppTheme {
        Surface {
            CartItem(
                product = ProductUI(
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
                productCount = 1,
                event = {}
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun CartScreenPreview() {
    FoodiesAppTheme {
        Surface {
            CartScreen(
                state = CartState(
                    products = mutableMapOf(
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
                        ) to 1,
                        ProductUI(
                            id = 312,
                            categoryID = 4124,
                            name = "Ролл Сяки Маки",
                            description = "Кокосовое молоко, кальмары, креветки, помидоры черри, грибы вешанки",
                            image = "someUrl",
                            priceCurrent = 48000,
                            priceOld = null,
                            measure = 400,
                            measureUnit = MeasureUnitUI.GR,
                            energyPer100Grams = 198.9f,
                            proteinsPer100Grams = 10f,
                            fatsPer100Grams = 8.5f,
                            carbohydratesPer100Grams = 19.7f,
                            tagIDS = listOf()
                        ) to 1,
                        ProductUI(
                            id = 11,
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
                        ) to 1
                    ),
                    totalPrice = 1920 * 100
                ),
                event = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun CartScreenEmptyPreview() {
    FoodiesAppTheme {
        Surface {
            CartScreen(
                state = CartState(products = mutableMapOf()),
                event = {},
                navigateUp = {}
            )
        }
    }
}