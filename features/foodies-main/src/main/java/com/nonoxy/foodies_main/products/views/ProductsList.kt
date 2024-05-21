package com.nonoxy.foodies_main.products.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nonoxy.foodies.main.R
import com.nonoxy.foodies_main.models.ProductUI
import com.nonoxy.foodies_main.models.TagUI

@Composable
internal fun ProductsList(products: List<ProductUI>, tags: List<TagUI>) {
    LazyColumn(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        itemsIndexed(items = products) { index, product ->
            if (index % 2 == 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ProductCard(
                        product = product,
                        tags = tags,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                    if (index + 1 < products.size) {
                        ProductCard(
                            product = products[index + 1],
                            tags = tags,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}


@Composable
private fun ProductCard(product: ProductUI, tags: List<TagUI>, modifier: Modifier) {
    Box(
        modifier = modifier.background(
            MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(8.dp)
        )
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (product.priceOld != null) {
                Image(
                    painter = painterResource(id = R.drawable.ic_discount_tag),
                    contentDescription = null,
                )
            }
            if (product.tagIDS.contains(tags.find { it.name == "Острое" }?.id)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_spicy_tag),
                    contentDescription = stringResource(R.string.tag_spicy),
                )
            }
            if (product.tagIDS.contains(tags.find { it.name == "Вегетарианское блюдо" }?.id)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_no_meat_tag),
                    contentDescription = "Тэг - вегетерианское блюдо",
                )
            }
        }
        Column {
            Image(
                painter = painterResource(id = R.drawable.food),
                contentDescription = null,
                modifier = Modifier.height(170.dp)
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
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                    .height(40.dp),
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
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductCardPreview() {
    //ProductCard()
}