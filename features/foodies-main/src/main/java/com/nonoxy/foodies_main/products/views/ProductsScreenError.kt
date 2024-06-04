package com.nonoxy.foodies_main.products.views

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nonoxy.foodies.main.R
import com.nonoxy.foodies_main.products.ProductsEvent
import com.nonoxy.foodies_theme.ui.theme.FoodiesAppTheme
import com.nonoxy.foodies_theme.ui.theme.common.roundRectShadow
import com.nonoxy.foodies_theme.ui.theme.common.shadow
import com.nonoxy.foodies_theme.ui.theme.entity.CustomShadowParams

@Composable
internal fun ProductsScreenError(
    event: (ProductsEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .roundRectShadow(
                    customShadowParams = CustomShadowParams.shadow2(),
                    cornerRadius = 8.dp
                )
                .background(
                    color = MaterialTheme.colorScheme.background,
                )
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 16.dp,
                    bottom = 8.dp
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.logo),
                contentDescription = null,
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.error_on_data_fetch),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = .6f),
                textAlign = TextAlign.Center
            )
            Button(
                onClick = { event(ProductsEvent.RefetchData) },
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(.6f)
                    .height(40.dp)
                    .shadow(blurRadius = 16.dp, spread = (-10).dp, offsetY = 20.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.primary)

            ) {
                Text(
                    text = stringResource(R.string.update),
                    style = MaterialTheme.typography.titleMedium.merge(lineHeight = 16.sp),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun ProductsScreenErrorPreview() {
    FoodiesAppTheme {
        Surface {
            ProductsScreenError(
                event = {}
            )
        }
    }
}