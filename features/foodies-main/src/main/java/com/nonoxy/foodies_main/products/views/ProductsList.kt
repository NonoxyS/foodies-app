package com.nonoxy.foodies_main.products.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nonoxy.foodies_main.models.ProductUI
import com.nonoxy.foodies_main.models.TagUI
import com.nonoxy.foodies_main.products.ProductsEvent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList

// По ТЗ надо было сделать LazyColumn для данного списка,
// но, естественно, LazyVerticalGrid - лучше подходит под задачу
@Composable
internal fun ProductsList(
    modifier: Modifier = Modifier,
    products: ImmutableList<ProductUI>,
    productsInCart: ImmutableMap<ProductUI, Int>,
    tags: ImmutableList<TagUI>,
    event: (ProductsEvent) -> Unit,
    navigateToDetail: (ProductUI) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        itemsIndexed(items = products, key = { _, item -> item.id } ) { index, product ->
            if (index % 2 == 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ProductCard(
                        product = product,
                        productCountInCart = productsInCart[product] ?: 0,
                        tags = tags,
                        event = event,
                        navigateToDetail = navigateToDetail,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    )
                    if (index + 1 < products.size) {
                        ProductCard(
                            product = products[index + 1],
                            productCountInCart = productsInCart[products[index + 1]] ?: 0,
                            tags = tags.toImmutableList(),
                            event = event,
                            navigateToDetail = navigateToDetail,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
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