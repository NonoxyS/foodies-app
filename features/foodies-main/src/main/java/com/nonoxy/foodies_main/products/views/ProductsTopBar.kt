package com.nonoxy.foodies_main.products.views

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nonoxy.foodies.main.R
import com.nonoxy.foodies_theme.ui.theme.FoodiesAppTheme
import com.nonoxy.foodies_theme.ui.theme.common.CustomSearchField

@Composable
internal fun ProductsTopBar(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onFilterClick: () -> Unit,
) {
    var isSearchOpen by rememberSaveable { mutableStateOf(false) }


    val focusRequester = remember { FocusRequester() }
    val localKeyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 16.dp,
                bottom = 0.dp
            )
            .fillMaxWidth()
            .height(44.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        AnimatedVisibility(
            visible = !isSearchOpen,
            enter = slideInHorizontally(),
            exit = shrinkHorizontally()
        ) {
            Row {
                IconButton(
                    modifier = Modifier.size(44.dp),
                    onClick = onFilterClick
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_filter),
                        contentDescription = stringResource(R.string.filter),
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }


                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.weight(1f)
                )


                IconButton(
                    modifier = Modifier.size(44.dp),
                    onClick = { isSearchOpen = !isSearchOpen }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                        contentDescription = stringResource(R.string.search),
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = isSearchOpen,
            enter = slideInHorizontally(),
            exit = shrinkHorizontally()
        ) {
            LaunchedEffect(key1 = Unit) {
                focusRequester.requestFocus()
            }
            CustomSearchField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = searchText,
                onValueChange = { onSearchTextChanged(it) },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        localKeyboardController?.hide()
                        localFocusManager.clearFocus()
                    }
                ),
                placeholder = {
                    Text(
                        text = "Найти блюдо",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = .6f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrowleft),
                        contentDescription = stringResource(R.string.back),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { isSearchOpen = !isSearchOpen }

                    )
                },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
                            contentDescription = stringResource(R.string.clear_input),
                            modifier = Modifier.clickable { onSearchTextChanged("") }
                        )
                    }
                }
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun ProductsTopBarPreview() {
    FoodiesAppTheme {
        Surface {
            ProductsTopBar(searchText = "", onSearchTextChanged = {}, onFilterClick = {})
        }
    }
}