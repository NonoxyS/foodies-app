package com.nonoxy.foodies_main.productdetail.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nonoxy.foodies_theme.ui.theme.FoodiesAppTheme

@Composable
internal fun EnergyValueRow(
    name: String,
    measure: String,
    measureUnit: String
) {
    Row(
        modifier = Modifier
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
    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = .3f)
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun EnergyValueRowPreview() {
    FoodiesAppTheme {
        Surface {
            EnergyValueRow(
                name = "Вес",
                measure = "400",
                measureUnit = "Г"
            )
        }
    }
}