package com.example.producttesttask.ui.view_models

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.producttesttask.remote.serializables.Product
import com.example.producttesttask.repositories.ProductRepository
import com.example.producttesttask.ui.sorting.SortType
import com.example.producttesttask.ui.state.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class RemoteViewModel(
    private val repository: ProductRepository
): ViewModel() {
    private val _sortType = MutableStateFlow(SortType.NONE)
    val sortedProducts = _sortType.flatMapLatest { sortType ->
        repository.getSortedProducts(sortType)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ResultState.Loading())

    fun getProducts() : Flow<PagingData<Product>> = repository.getProducts().cachedIn(viewModelScope)

    private val _resultState = MutableStateFlow<ResultState>(ResultState.Loading())
    val resultState: StateFlow<ResultState> get() = _resultState

    fun search(query: String) {
        viewModelScope.launch {
            _resultState.value = ResultState.Loading()
            try {
                repository.search(query).collect { result ->
                    _resultState.value = result
                }
            } catch (e: Exception) {
                _resultState.value = ResultState.Error(e)
            }
        }
    }

    fun setSortType(sortType: SortType){
        _sortType.update { sortType }
    }
}