package com.example.producttesttask.remote

import com.example.producttesttask.remote.serializables.Products
import com.example.producttesttask.ui.sorting.SortType
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RemoteApi {
    private val url = "https://dummyjson.com/products"
    private val client = OkHttpClient()


    suspend fun getProducts(skip: Int, limit: Int = 20): Products = suspendCoroutine { continuation ->
        val request = Request.Builder()
            .url("$url?skip=$skip&limit=$limit")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val products = Json.decodeFromString<Products>(response.body?.string()!!)
                    continuation.resume(products)
                } else {
                    continuation.resumeWithException(IOException("Unexpected response code: ${response.code}"))
                }
            }
        })
    }

    suspend fun search(query: String): Products = suspendCoroutine { continuation ->
        val request = Request.Builder()
            .url("$url/search?q=$query")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val products = Json.decodeFromString<Products>(response.body?.string()!!)
                    continuation.resume(products)
                } else {
                    continuation.resumeWithException(IOException("Unexpected response code: ${response.code}"))
                }
            }
        })
    }

    suspend fun getSortedProducts(sortType: SortType): Products = suspendCoroutine { continuation ->
        val request = Request.Builder()
            .url("$url/category/${sortType.title}")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val products = Json.decodeFromString<Products>(response.body?.string()!!)
                    continuation.resume(products)
                } else {
                    continuation.resumeWithException(IOException("Unexpected response code: ${response.code}"))
                }
            }
        })
    }
}