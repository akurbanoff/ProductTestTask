package com.example.producttesttask.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.producttesttask.utils.safePopBackStack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopBar(
    navigator: NavHostController
) {
    TopAppBar(
        title = { /*TODO*/ },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier.clickable { navigator.safePopBackStack() }.size(44.dp)
            )
        }
    )
}