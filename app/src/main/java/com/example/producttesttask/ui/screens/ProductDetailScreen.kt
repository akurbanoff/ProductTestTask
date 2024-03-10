package com.example.producttesttask.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.producttesttask.remote.serializables.Product

@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProductDetailScreen(
    product: Product?,
    navigator: NavHostController
) {
    if(product != null)
    Scaffold(
        topBar = { DefaultTopBar(navigator = navigator) },
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White)
    ) { paddingValues ->
        Card(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color.White),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                HorizontalPager(
                    state = rememberPagerState { product.images.size },
                    pageSpacing = 8.dp
                ) { page ->
                    println("Page - $page")
                    //currentPosition.value = page
                    GlideImage(
                        model = product.images[page],
                        contentDescription = null,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .fillMaxHeight(0.5f),
                        contentScale = ContentScale.FillBounds
                    )
                }
                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "${product.price}$",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Brand - ${product.brand}")
                Text(text = "Category - ${product.category}")
                Text(text = "Rating - ${product.rating}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.description)
            }
        }
    }
}