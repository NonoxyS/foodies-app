package com.nonoxy.foodies_main.productdetail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nonoxy.foodies.main.R
import com.nonoxy.foodies_main.models.MeasureUnitUI
import com.nonoxy.foodies_main.models.ProductUI
import com.nonoxy.foodies_main.productdetail.views.AddProductToCartButton
import com.nonoxy.foodies_main.productdetail.views.ChangeProductCountInCartButtons
import com.nonoxy.foodies_main.productdetail.views.EnergyValueRow
import com.nonoxy.foodies_theme.ui.theme.FoodiesAppTheme
import com.nonoxy.foodies_theme.ui.theme.common.FImage
import com.nonoxy.foodies_theme.ui.theme.common.shadow


@Composable
fun ProductDetailScreen(
    product: ProductUI,
    state: ProductDetailState,
    event: (ProductDetailEvent) -> Unit,
    navigateUp: () -> Unit,
    navigateToCart: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            FImage(
                drawableResId = R.drawable.food,
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
            EnergyValueRow(
                name = "Вес",
                measure = "${product.measure}",
                measureUnit = product.measureUnit.value
            )
            EnergyValueRow(
                name = "Энерг. ценность",
                measure = "${product.energyPer100Grams}",
                measureUnit = "ккал"
            )
            EnergyValueRow(
                name = "Белки",
                measure = "${product.proteinsPer100Grams}",
                measureUnit = MeasureUnitUI.GR.value
            )
            EnergyValueRow(
                name = "Жиры",
                measure = "${product.fatsPer100Grams}",
                measureUnit = MeasureUnitUI.GR.value
            )
            EnergyValueRow(
                name = "Углеводы",
                measure = "${product.carbohydratesPer100Grams}",
                measureUnit = MeasureUnitUI.GR.value
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (!state.productsInCart.contains(product)) {
                AddProductToCartButton(product = product, event = event)
            } else {
                ChangeProductCountInCartButtons(
                    product = product,
                    productCount = state.productsInCart[product] ?: 1,
                    event = event,
                    navigateToCart = navigateToCart
                )
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
                .shadow(blurRadius = 16.dp, spread = (-10).dp, offsetY = 15.dp)
                .clip(CircleShape)
                .size(44.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            onClick = { navigateUp() },
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrowleft),
                contentDescription = null
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun ProductDetailScreenPreview() {
    FoodiesAppTheme {
        Surface {
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
                state = ProductDetailState(),
                navigateToCart = {},
                navigateUp = {}
            )
        }
    }
}