package com.nonoxy.foodies_main.productdetail.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nonoxy.foodies.main.R
import com.nonoxy.foodies_main.models.MeasureUnitUI
import com.nonoxy.foodies_main.models.ProductUI
import com.nonoxy.foodies_main.productdetail.ProductDetailEvent
import com.nonoxy.foodies_theme.ui.theme.FoodiesAppTheme
import com.nonoxy.foodies_theme.ui.theme.common.shadow


@Composable
internal fun ChangeProductCountInCartButtons(
    product: ProductUI,
    productCount: Int,
    event: (ProductDetailEvent) -> Unit,
    navigateToCart: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = { event(ProductDetailEvent.DownProductCountInCart(product)) },
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
                contentDescription = stringResource(R.string.product_count_decrease)
            )
        }

        Text(
            text = "$productCount",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(32.dp)
        )

        Button(
            onClick = { event(ProductDetailEvent.UpProductCountInCart(product)) },
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
                contentDescription = stringResource(R.string.product_count_increase)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            modifier = Modifier
                .height(48.dp)
                .shadow(blurRadius = 16.dp, spread = (-5).dp, offsetY = 20.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            onClick = { navigateToCart() }
        ) {
            Text(
                text = stringResource(R.string.toCart),
                style = MaterialTheme.typography.titleMedium.merge(lineHeight = 16.sp),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun ChangeProductCountInCartButtonsPreview() {
    FoodiesAppTheme {
        Surface {
            Column {
                ChangeProductCountInCartButtons(
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
                    event = {},
                    navigateToCart = {}
                )
                ChangeProductCountInCartButtons(
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
                    productCount = 2,
                    event = {},
                    navigateToCart = {}
                )
            }
        }
    }
}