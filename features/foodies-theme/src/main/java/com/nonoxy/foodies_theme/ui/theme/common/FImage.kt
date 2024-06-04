package com.nonoxy.foodies_theme.ui.theme.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource


/**
 * Избегает лишних рекомпозиций у растровых изображений
 */
@Composable
fun FImage(
    modifier: Modifier = Modifier,
    drawableResId: Int,
    contentScale: ContentScale = ContentScale.Fit,
    contentDescription: String? = null
) {
    Image(
        painter = painterResource(id = drawableResId),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    )
}