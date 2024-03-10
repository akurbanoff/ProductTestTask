package com.example.producttesttask.remote.serializables

import kotlinx.serialization.Serializable

@Serializable
data class Products(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

@Serializable
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>
)
