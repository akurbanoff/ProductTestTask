package com.example.producttesttask.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.producttesttask.remote.RemoteApi
import com.example.producttesttask.remote.serializables.Product
import kotlinx.coroutines.delay
import java.lang.Exception

class ProductPagerSource(
    private val api: RemoteApi
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(20)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(20)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val skip = params.key ?: 0
            val response = api.getProducts(skip = skip)
            delay(1000L) // тестовая задержка чтобы показать исполнение условия с последовательной загрузкой

            LoadResult.Page(
                data = response.products,
                prevKey = if(skip == 0) null else skip.minus(20),
                nextKey = if(response.products.isEmpty()) null else skip.plus(20)
            )
        } catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}