package com.nonoxy.foodies_main.cart

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nonoxy.foodies.main.R
import com.nonoxy.foodies_main.common.shadow
import com.nonoxy.foodies_main.models.MeasureUnitUI
import com.nonoxy.foodies_main.models.ProductUI

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
                    text = "Корзина",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 32.dp)
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = navigateUp,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrowleft),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .shadow(
                    blurRadius = 16.dp,
                    color = Color.Gray,
                    offsetY = 4.dp,
                    spread = (-5).dp
                )
        )
        CartList(state = state, event = event)
    }
}

@Composable
private fun CartList(state: CartState, event: (CartEvent) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items = state.products.toList(), key = { it.first.id }) { (product, productCount) ->
            CartItem(product = product, productCount = productCount, event = event)
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = .3f)
            )
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
            .padding(16.dp)
            .height(130.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.food),
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { event(CartEvent.DownProductCount(product)) },
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_minus),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "$productCount",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(32.dp)
                )

                IconButton(
                    onClick = { event(CartEvent.UpProductCount(product)) },
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = null,
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

@Composable
@Preview(showSystemUi = true, showBackground = true)
private fun CartItemPreview() {
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

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun CartScreenPreview() {
    CartScreen(
        CartState(
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
            )
        ),
        event = {},
        navigateUp = {}
    )
}