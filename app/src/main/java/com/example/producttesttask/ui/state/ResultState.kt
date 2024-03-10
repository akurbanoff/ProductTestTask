package com.example.producttesttask.ui.state

import com.example.producttesttask.remote.serializables.Product

sealed class ResultState {
    data class Error(
        val throwable: Throwable
    ) : ResultState()

    data class Success(
        val products: List<Product>
    ): ResultState()

    class Loading : ResultState()
}