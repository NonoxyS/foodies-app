package com.nonoxy.foodies_main.products.views

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nonoxy.foodies.main.R
import com.nonoxy.foodies_main.models.MeasureUnitUI
import com.nonoxy.foodies_main.models.ProductUI
import com.nonoxy.foodies_main.models.TagUI
import com.nonoxy.foodies_main.products.ProductsEvent
import com.nonoxy.foodies_theme.ui.theme.FoodiesAppTheme
import com.nonoxy.foodies_theme.ui.theme.common.FImage
import com.nonoxy.foodies_theme.ui.theme.common.shadow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ProductCard(
    product: ProductUI,
    productCountInCart: Int,
    tags: ImmutableList<TagUI>,
    event: (ProductsEvent) -> Unit,
    navigateToDetail: (ProductUI) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { navigateToDetail(product) }
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (product.priceOld != null) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_discount_tag),
                    contentDescription = stringResource(R.string.tag_discount),
                )
            }
            if (product.tagIDS.contains(tags.find { it.name == "Острое" }?.id)) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_spicy_tag),
                    contentDescription = stringResource(R.string.tag_spicy),
                )
            }
            if (product.tagIDS.contains(tags.find { it.name == "Вегетарианское блюдо" }?.id)) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_no_meat_tag),
                    contentDescription = stringResource(R.string.tag_vegan),
                )
            }
        }
        Column {
            FImage(
                drawableResId = R.drawable.food,
                contentDescription = null,
                modifier = Modifier
                    .height(170.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${product.measure} ${product.measureUnit.value}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            if (productCountInCart <= 0) {
                AddProductToCartButton(product = product, event = event)
            } else {
                ChangeProductCountInCartButtons(
                    product = product,
                    productCount = productCountInCart,
                    event = event
                )
            }
        }
    }
}

@Composable
private fun AddProductToCartButton(
    product: ProductUI,
    event: (ProductsEvent.AddProductToCart) -> Unit,
) {
    Button(
        onClick = { event(ProductsEvent.AddProductToCart(product)) },
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .height(40.dp)
            .shadow(blurRadius = 16.dp, spread = (-10).dp, offsetY = 20.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.background)

    ) {
        Text(
            text = "${product.priceCurrent / 100} ₽",
            style = MaterialTheme.typography.titleMedium.merge(lineHeight = 16.sp),
            color = MaterialTheme.colorScheme.onBackground,
        )
        if (product.priceOld != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${product.priceOld / 100} ₽",
                style = MaterialTheme.typography.bodyMedium.merge(
                    textDecoration = TextDecoration.LineThrough,
                    lineHeight = 16.sp
                ),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            )
        }
    }
}

@Composable
private fun ChangeProductCountInCartButtons(
    product: ProductUI,
    productCount: Int,
    event: (ProductsEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { event(ProductsEvent.DownProductCountInCart(product)) },
            modifier = Modifier
                .size(40.dp)
                .shadow(blurRadius = 16.dp, spread = (-10).dp, offsetY = 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = if (productCount > 1) ImageVector.vectorResource(id = R.drawable.ic_minus)
                else ImageVector.vectorResource(id = R.drawable.ic_delete),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "$productCount",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(32.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            onClick = { event(ProductsEvent.UpProductCountInCart(product)) },
            modifier = Modifier
                .size(40.dp)
                .shadow(blurRadius = 16.dp, spread = (-10).dp, offsetY = 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_plus),
                contentDescription = null
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun ProductCardPreview() {
    FoodiesAppTheme {
        Surface {
            Column {
                ProductCard(
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
                    productCountInCart = 0,
                    tags = persistentListOf(),
                    event = {},
                    navigateToDetail = {},
                    modifier = Modifier
                        .height(IntrinsicSize.Max)
                        .width(IntrinsicSize.Max)
                )
                Spacer(modifier = Modifier.height(4.dp))
                ProductCard(
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
                    productCountInCart = 1,
                    tags = persistentListOf(),
                    event = {},
                    navigateToDetail = {},
                    modifier = Modifier
                        .height(IntrinsicSize.Max)
                        .width(IntrinsicSize.Max)
                )
            }
        }
    }
}