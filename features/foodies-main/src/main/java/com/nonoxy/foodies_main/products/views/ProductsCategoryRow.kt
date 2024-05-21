package com.nonoxy.foodies_main.products.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nonoxy.foodies_main.models.CategoryUI

@Composable
internal fun ProductsCategoryRow(
    categories: List<CategoryUI>,
    selectedCategoryId: Long,
    onCategorySelected: (Long) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item { Spacer(modifier = Modifier.width(8.dp)) }

        items(
            items = categories,
            key = { category ->
                category.id
            }
        ) { category ->
            Category(category = category, isSelected = selectedCategoryId == category.id) {
                onCategorySelected(category.id)
            }
        }

        item { Spacer(modifier = Modifier.width(8.dp)) }
    }
}

@Composable
private fun Category(
    category: CategoryUI,
    isSelected: Boolean,
    onCategorySelect: () -> Unit,
) {

    val buttonColor = if (!isSelected) {
        ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    } else {
        ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.background
        )
    }

    TextButton(
        onClick = {
            onCategorySelect.invoke()
        },
        shape = RoundedCornerShape(8.dp),
        colors = buttonColor,
        modifier = Modifier.height(40.dp)
    ) {
        Text(
            text = category.name,
            style = MaterialTheme.typography.titleMedium,
            lineHeight = 16.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}


@Preview
@Composable
private fun ProductsCategoryRowPreview() {
    val categories = listOf(
        CategoryUI(1, "Роллы"),
        CategoryUI(12, "Суши"),
        CategoryUI(17, "Наборы"),
        CategoryUI(11, "Горячие блюда"),
        CategoryUI(31, "Супы"),
        CategoryUI(21, "Десерты")
    )
    ProductsCategoryRow(categories = categories, selectedCategoryId = 1, onCategorySelected = {})
}