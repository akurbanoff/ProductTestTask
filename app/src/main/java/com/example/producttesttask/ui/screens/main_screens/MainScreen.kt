package com.example.producttesttask.ui.screens.main_screens

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.producttesttask.remote.serializables.Product
import com.example.producttesttask.ui.navigation.NavRoutes
import com.example.producttesttask.ui.sorting.SortType
import com.example.producttesttask.ui.state.ResultState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    products: LazyPagingItems<Product>,
    navigator: NavHostController,
    searchQuery: MutableState<String>,
    onSearch: () -> Unit,
    searchState: State<ResultState>,
    sortType: MutableState<SortType>,
    sortState: State<ResultState>,
    onSetSortType: () -> Unit,
    currentProductList: MutableState<List<Product>>
) {
    val searchJob = remember { mutableStateOf<Job?>(null) }
    var showSortMenu by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TextField(
                value = searchQuery.value,
                onValueChange = {
                    searchQuery.value = it
                    searchJob.value?.cancel()
                    searchJob.value = CoroutineScope(Dispatchers.Main).launch {
                        delay(1000) // задержка в миллисекундах для ожидания окончания ввода
                        onSearch()
                    }
                                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.medium),
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.medium
            )
        },
        modifier = Modifier.padding(16.dp)
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Sort by:")
                Spacer(modifier = Modifier.width(4.dp))
                ExposedDropdownMenuBox(
                    modifier = Modifier.clickable { showSortMenu = true },
                    expanded = showSortMenu,
                    onExpandedChange = { showSortMenu = !showSortMenu }) {
                    TextField(
                        value = sortType.value.title,
                        onValueChange = { showSortMenu = true},
                        readOnly = true,
                        shape = MaterialTheme.shapes.medium,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.LightGray,
                            unfocusedContainerColor = Color.LightGray,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = MaterialTheme.shapes.medium
                            )
                    )
                    ExposedDropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }) {
                        SortType.getSortTypes().forEach { currentSortType ->
                            DropdownMenuItem(
                                text = { Text(
                                    text = if(currentSortType == SortType.NONE) "nothing" else currentSortType.title
                                ) },
                                onClick = {
                                    sortType.value = currentSortType
                                    onSetSortType()
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                }
            }
            if (searchQuery.value.isNotEmpty()){
                when(val state = searchState.value){
                    is ResultState.Success -> {
                        currentProductList.value = state.products
                        DefaultProductList(
                            products = state.products,
                            navigator = navigator
                        )
                    }
                    is ResultState.Error -> MainScreenError(error = state.throwable.message) {
                        searchQuery.value = ""
                    }
                    is ResultState.Loading -> MainScreenLoading()
                }
            } else if (sortType.value != SortType.NONE) {
                when(val state = sortState.value){
                    is ResultState.Error -> MainScreenError(error = state.throwable.message) {
                        sortType.value = SortType.NONE
                    }
                    is ResultState.Loading -> MainScreenLoading(modifier = Modifier.align(Alignment.CenterHorizontally))
                    is ResultState.Success -> {
                        currentProductList.value = state.products
                        DefaultProductList(
                            products = state.products,
                            navigator = navigator
                        )
                    }
                }
            } else {
                PagingProductList(
                    products = products,
                    navigator = navigator,
                    currentProductList = currentProductList
                )
            }
        }
    }
}

@Composable
fun PagingProductList(
    modifier: Modifier = Modifier,
    products: LazyPagingItems<Product>,
    navigator: NavHostController,
    currentProductList: MutableState<List<Product>>
) {
    LazyColumn(
        modifier = modifier,
        flingBehavior = ScrollableDefaults.flingBehavior()
    ) {
        items(
            items = products
        ) { product ->
            currentProductList.value = products.itemSnapshotList.items
            ProductElement(
                product = product,
                index = product?.id!!,
                modifier = Modifier.padding(8.dp),
                navigator = navigator
            )
        }
        when( val state = products.loadState.refresh){
            is LoadState.Error -> item{MainScreenError(state.error.message, onRefresh = { products.refresh() })}
            LoadState.Loading -> item{
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Loading",
                        modifier = Modifier.padding(8.dp)
                    )
                    CircularProgressIndicator(color = Color.Black)
                }
            }
            else -> {}
        }
        when( val state = products.loadState.append ){
            is LoadState.Error -> item { MainScreenError(state.error.message, onRefresh = { products.refresh() }) }
            LoadState.Loading -> item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Loading",
                        modifier = Modifier.padding(8.dp)
                    )
                    CircularProgressIndicator(color = Color.Black)
                }
            }
            else -> {}
        }
    }
}

@Composable
fun DefaultProductList(
    modifier: Modifier = Modifier,
    products: List<Product>,
    navigator: NavHostController
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(products){ product ->
            ProductElement(product = product, index = product.id, navigator = navigator, modifier = Modifier.padding(8.dp))
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductElement(modifier: Modifier = Modifier, product: Product?, index: Int, navigator: NavHostController) {
    if(product != null)
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        GlideImage(
            model = product.thumbnail,
            contentDescription = null,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .height(200.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = product.title,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "Price - ${product.price}$(-${product.discountPercentage}%)",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Text(text = "Brand - ${product.brand}")
        Text(text = "Category - ${product.category}")
        Text(text = "Rating - ${product.rating}")
        Text(
            text = if(product.description.length > 50) product.description.take(30) + "..." else product.description,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Start)
        )
        Button(
            onClick = { navigator.navigate(NavRoutes.ProductDetailScreen.withArgs(index)) },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "See detail",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}