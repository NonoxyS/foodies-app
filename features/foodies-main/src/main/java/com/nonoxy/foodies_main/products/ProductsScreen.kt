package com.nonoxy.foodies_main.products

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nonoxy.foodies.main.R
import com.nonoxy.foodies_main.products.views.ProductsCategoryRow
import com.nonoxy.foodies_main.products.views.ProductsList
import com.nonoxy.foodies_main.products.views.ProductsTopBar
import kotlinx.coroutines.launch

@Composable
fun ProductsScreen() {
    ProductsScreen(viewModel = viewModel<ProductsMainViewModel>())
}

@Composable
internal fun ProductsScreen(viewModel: ProductsMainViewModel) {

    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is ProductsScreenState.Success -> {
            Products(
                state = currentState,
                onCategorySelected = { viewModel.updateSelectedCategoryId(it) },
                onSearchTextChanged = { viewModel.updateSearchText(it) },
                onFilterClick = { viewModel.toggleFilterOpen() },
                onFilterSelected = { categoryName, isSelected ->
                    viewModel.updateSelectedFilters(categoryName, isSelected)
                },
                onDoneClicked = { viewModel.onFilterDoneButton() }
            )
        }

        is ProductsScreenState.Error -> Log.d("Foodies", "$currentState")
        is ProductsScreenState.Loading -> Log.d("Foodies", "$currentState")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Products(
    state: ProductsScreenState.Success,
    onCategorySelected: (Long) -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onFilterClick: () -> Unit,
    onFilterSelected: (String, Boolean) -> Unit,
    onDoneClicked: () -> Unit,
) {

    Column {
        ProductsTopBar(
            searchText = state.searchText,
            onSearchTextChanged = onSearchTextChanged,
            onFilterClick = onFilterClick
        )
        ProductsCategoryRow(
            categories = state.categories,
            selectedCategoryId = state.selectedCategoryId,
            onCategorySelected = onCategorySelected
        )
        BottomShadow(height = 8.dp)
        ProductsList(products = state.filteredProducts, tags = state.tags)
    }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (state.isFilterOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    onFilterClick()
                }
            },
        ) {
            Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 32.dp)) {
                Text(
                    text = "Подобрать блюда",
                    style = MaterialTheme.typography.titleLarge.copy(letterSpacing = 0.15.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                FilterItem(
                    name = "Без мяса",
                    isSelected = state.selectedFilters["Без мяса"] ?: false,
                    onFilterSelected = { categoryName, isSelected -> onFilterSelected(categoryName, isSelected) }
                )
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
                FilterItem(
                    name = "Острое",
                    isSelected = state.selectedFilters["Острое"] ?: false,
                    onFilterSelected = { categoryName, isSelected -> onFilterSelected(categoryName, isSelected) }
                )
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
                FilterItem(
                    name = "Со скидкой",
                    isSelected = state.selectedFilters["Со скидкой"] ?: false,
                    onFilterSelected = { categoryName, isSelected -> onFilterSelected(categoryName, isSelected) }
                )

                TextButton(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            onDoneClicked()
                        }
                        Log.d("products", "${state.products}")
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text(
                        text = stringResource(R.string.done),
                        style = MaterialTheme.typography.titleMedium.copy(lineHeight = 16.sp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun FilterItem(
    name: String,
    isSelected: Boolean,
    onFilterSelected: (String, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .height(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onFilterSelected(name, !it) },
            modifier = Modifier.scale(1.2f),  // 20 * 1.2 = 24 dp
            colors = CheckboxDefaults.colors().copy(uncheckedBorderColor = Color.Gray)
        )
    }
}


@Composable
fun BottomShadow(alpha: Float = 0.1f, height: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = alpha),
                        Color.Transparent,
                    )
                )
            )
    )
}


/*
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
internal fun SearchDefinitionBar(
    modifier: Modifier,
    onSearch: (String, String) -> Unit,
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    val localKeyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current

    Row(modifier = modifier) {
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(text = "Search") },
            singleLine = true,
            shape = SearchBarDefaults.inputFieldShape,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    localKeyboardController?.hide()
                    localFocusManager.clearFocus()
                    onSearch(text, selectedLanguage)
                }
            ),
            trailingIcon = {
                if (text.isEmpty()) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear",
                        modifier = Modifier.clickable(onClick = { text = "" })
                    )
                }
            },
            modifier = Modifier.onPreInterceptKeyBeforeSoftKeyboard {
                if (it.key == Key.Back) {
                    localFocusManager.clearFocus()
                }
                false
            }
        )

        var expanded by remember { mutableStateOf(false) }
        // Тупо перенёс енум из апи слоя сюда, надо будет переделать этот момент
        // В апи слое он ничего не делает
        val languages by remember { mutableStateOf(Language.entries.map { it.lang }) }

        Log.d("langTest", "$languages")

        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = selectedLanguage,
                onValueChange = { },
                singleLine = true,
                enabled = false,
                shape = SearchBarDefaults.inputFieldShape,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier.clickable { expanded = !expanded }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = !expanded },
                modifier = Modifier
                    .height(200.dp)
            ) {
                languages.forEach { lang ->
                    DropdownMenuItem(
                        text = { Text(text = lang.uppercase()) },
                        onClick = {
                            onLanguageSelected(lang.uppercase())
                            expanded = !expanded
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Definitions(
    definitions: List<CategoriesUI>,
    onSearch: (String, String) -> Unit,
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    Log.d("sTest", "UI data -> $definitions")
    Column(modifier = Modifier.padding(8.dp)) {
        SearchDefinitionBar(
            modifier = Modifier.fillMaxWidth(),
            onSearch = onSearch,
            selectedLanguage = selectedLanguage,
            onLanguageSelected = onLanguageSelected,
        )
        LazyColumn {
            items(definitions) { definition ->
                key(definition.text, definition.pos) {
                    Definition(definition)
                }
            }
        }
    }
}

@Composable
internal fun Definition(definition: CategoriesUI) {
    Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = definition.text, style = MaterialTheme.typography.bodyLarge)
            definition.transcription?.let {
                Text(
                    text = "[${definition.transcription}]",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Text(
            text = definition.translate?.get(0)?.text ?: "no translate",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(text = definition.pos, style = MaterialTheme.typography.bodyLarge)
    }
}*/
