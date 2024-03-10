package com.example.producttesttask.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.producttesttask.pager.ProductPagerSource
import com.example.producttesttask.remote.RemoteApi
import com.example.producttesttask.ui.sorting.SortType
import com.example.producttesttask.ui.state.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ProductRepository(
    private val api: RemoteApi
) {
    fun getProducts() = Pager(
        config = PagingConfig(
            pageSize = 1
        ),
        pagingSourceFactory = {ProductPagerSource(api = api)}
    ).flow

    suspend fun search(query: String): Flow<ResultState> {
        return try {
            flowOf(ResultState.Success(api.search(query).products))
        } catch (e: Exception){
            flowOf(ResultState.Error(e))
        }
    }

    suspend fun getSortedProducts(sortType: SortType): Flow<ResultState>{
        return if(sortType != SortType.NONE) {
            try {
                flowOf(ResultState.Success(api.getSortedProducts(sortType).products))
            } catch (e: Exception){
                flowOf(ResultState.Error(e))
            }
        } else {
            flowOf(ResultState.Success(emptyList()))
        }
    }
}