package com.example.producttesttask.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.producttesttask.remote.serializables.Product
import com.example.producttesttask.ui.screens.ProductDetailScreen
import com.example.producttesttask.ui.screens.main_screens.MainScreen
import com.example.producttesttask.ui.sorting.SortType
import com.example.producttesttask.ui.view_models.RemoteViewModel

@Composable
fun Navigator(remoteViewModel: RemoteViewModel){
    val navigator = rememberNavController()

    val state = remoteViewModel.getProducts().collectAsLazyPagingItems()
    val searchState = remoteViewModel.resultState.collectAsState()
    val searchQuery = remember{ mutableStateOf("") }
    val currentProductList = remember {
        mutableStateOf(emptyList<Product>())
    }

    val sortType = remember{ mutableStateOf(SortType.NONE) }
    val sortState = remoteViewModel.sortedProducts.collectAsState()

    NavHost(navController = navigator, startDestination = NavRoutes.MainScreen.route){
        composable(NavRoutes.MainScreen.route){
            MainScreen(
                products = state,
                navigator = navigator,
                searchQuery = searchQuery,
                onSearch = { remoteViewModel.search(searchQuery.value) },
                searchState = searchState,
                sortType = sortType,
                sortState = sortState,
                onSetSortType = { remoteViewModel.setSortType(sortType.value) },
                currentProductList = currentProductList
            )
        }
        composable(
            route = NavRoutes.ProductDetailScreen.route + "/{id}",
            arguments = listOf(navArgument("id"){type = NavType.IntType})){ navBackStackEntry ->
            navBackStackEntry.arguments.let { bundle ->
                val id = bundle?.getInt("id")
                currentProductList.value.forEach { product ->
                    if(product.id == id!!) ProductDetailScreen(product = product, navigator = navigator)
                }
            }
        }
    }
}