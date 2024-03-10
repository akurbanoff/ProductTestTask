package com.example.producttesttask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.producttesttask.remote.RemoteApi
import com.example.producttesttask.repositories.ProductRepository
import com.example.producttesttask.ui.navigation.Navigator
import com.example.producttesttask.ui.theme.ProductTestTaskTheme
import com.example.producttesttask.ui.view_models.RemoteViewModel

class MainActivity : ComponentActivity() {

    val remoteApi by lazy {
        RemoteApi()
    }

    val repository by lazy {
        ProductRepository(remoteApi)
    }

    val remoteViewModel by viewModels<RemoteViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RemoteViewModel(repository) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductTestTaskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigator(remoteViewModel = remoteViewModel)
                }
            }
        }
    }
}