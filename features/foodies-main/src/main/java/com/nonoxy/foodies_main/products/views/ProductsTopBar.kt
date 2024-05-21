package com.nonoxy.foodies_main.products.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nonoxy.foodies.main.R

@Composable
internal fun ProductsTopBar(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onFilterClick: () -> Unit,
) {
    var isSearchOpen by remember { mutableStateOf(false) }


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
        if (!isSearchOpen) {
            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = stringResource(R.string.filter),
                    modifier = Modifier.clickable { onFilterClick() }
                )
            }

            Box(
                modifier = Modifier
                    .padding()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                )
            }

            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = stringResource(R.string.search),
                    modifier = Modifier.clickable { isSearchOpen = !isSearchOpen }
                )
            }
        } else {
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
                        painter = painterResource(id = R.drawable.ic_arrowleft),
                        contentDescription = stringResource(R.string.back),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { isSearchOpen = !isSearchOpen }

                    )
                },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cancel),
                            contentDescription = stringResource(R.string.clear_input),
                            modifier = Modifier.clickable { onSearchTextChanged("") }
                        )
                    }
                }
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ProductsTopBarPreview() {
    ProductsTopBar(searchText = "", onSearchTextChanged = {}, onFilterClick = {})
}