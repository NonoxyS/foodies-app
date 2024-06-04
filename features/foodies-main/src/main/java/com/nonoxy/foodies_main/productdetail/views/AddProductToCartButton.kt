package com.nonoxy.foodies_main.productdetail.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nonoxy.foodies_main.models.MeasureUnitUI
import com.nonoxy.foodies_main.models.ProductUI
import com.nonoxy.foodies_main.productdetail.ProductDetailEvent
import com.nonoxy.foodies_theme.ui.theme.FoodiesAppTheme


@Composable
internal fun AddProductToCartButton(
    product: ProductUI,
    event: (ProductDetailEvent.AddProductToCart) -> Unit
) {
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
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            onClick = { event(ProductDetailEvent.AddProductToCart(product)) }
        ) {
            Text(
                text = "В корзину за ${product.priceCurrent / 100} ₽",
                style = MaterialTheme.typography.titleMedium.merge(lineHeight = 16.sp),
                color = MaterialTheme.colorScheme.onPrimary,
            )
            if (product.priceOld != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${product.priceOld / 100} ₽",
                    style = MaterialTheme.typography.bodyMedium.merge(
                        textDecoration = TextDecoration.LineThrough,
                        lineHeight = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                )
            }
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun AddProductToCartButtonPreview() {
    FoodiesAppTheme {
        Surface {
            Column {
            AddProductToCartButton(
                product = ProductUI(
                    id = 123,
                    categoryID = 4124,
                    name = "Том Ям",
                    description = "Кокосовое молоко, кальмары, креветки, помидоры черри, грибы вешанки",
                    image = "someUrl",
                    priceCurrent = 72000,
                    priceOld = null,
                    measure = 400,
                    measureUnit = MeasureUnitUI.GR,
                    energyPer100Grams = 198.9f,
                    proteinsPer100Grams = 10f,
                    fatsPer100Grams = 8.5f,
                    carbohydratesPer100Grams = 19.7f,
                    tagIDS = listOf()
                ),
                event = {}
            )
            AddProductToCartButton(
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
                event = {}
            )
        }
            }
    }
}