package com.nonoxy.foodies_main.productdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nonoxy.foodies.main.R
import com.nonoxy.foodies_main.cart.CartEvent
import com.nonoxy.foodies_main.cart.CartState
import com.nonoxy.foodies_main.models.MeasureUnitUI
import com.nonoxy.foodies_main.models.ProductUI


@Composable
fun ProductDetailScreen(
    product: ProductUI,
    cartState: CartState,
    event: (CartEvent) -> Unit,
    navigateUp: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        FloatingActionButton(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
                .size(44.dp)
                .clip(CircleShape)
                .align(Alignment.TopStart)
                .clickable { navigateUp() },
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.secondary,
            onClick = { navigateUp() },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrowleft),
                contentDescription = null
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.food),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(375.dp)
            )
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 34.sp,
                    lineHeight = 36.sp
                ),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp)
            )
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = .6f),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = .3f)
            )
            EnergyValues(
                name = "Вес",
                measure = "${product.measure}",
                measureUnit = product.measureUnit.value
            )
            EnergyValues(
                name = "Энерг. ценность",
                measure = "${product.energyPer100Grams}",
                measureUnit = "ккал"
            )
            EnergyValues(
                name = "Белки",
                measure = "${product.proteinsPer100Grams}",
                measureUnit = MeasureUnitUI.GR.value
            )
            EnergyValues(
                name = "Жиры",
                measure = "${product.fatsPer100Grams}",
                measureUnit = MeasureUnitUI.GR.value
            )
            EnergyValues(
                name = "Углеводы",
                measure = "${product.carbohydratesPer100Grams}",
                measureUnit = MeasureUnitUI.GR.value
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.background
                    ),
                    onClick = { event(CartEvent.AddProduct(product)) }
                ) {
                    Text(
                        text = "В корзину за ${product.priceCurrent / 100} ₽",
                        style = MaterialTheme.typography.titleMedium.merge(lineHeight = 16.sp),
                        color = MaterialTheme.colorScheme.background,
                    )
                    if (product.priceOld != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${product.priceOld / 100} ₽",
                            style = MaterialTheme.typography.bodyMedium.merge(
                                textDecoration = TextDecoration.LineThrough,
                                lineHeight = 16.sp
                            ),
                            color = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EnergyValues(
    name: String,
    measure: String,
    measureUnit: String
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .6f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 13.dp, bottom = 13.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "$measure $measureUnit",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(end = 16.dp, top = 13.dp, bottom = 13.dp)
        )
    }
    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = .3f))
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
private fun ProductDetailScreenPreview() {
    ProductDetailScreen(
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
        event = {},
        cartState = CartState(),
        navigateUp = {}
    )
}