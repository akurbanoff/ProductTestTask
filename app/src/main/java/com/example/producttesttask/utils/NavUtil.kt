package com.example.producttesttask.utils

import androidx.navigation.NavHostController
import com.example.producttesttask.ui.navigation.NavRoutes

fun NavHostController.safePopBackStack(): Boolean{
    return if(this.currentBackStackEntry?.destination?.route!! != NavRoutes.MainScreen.route) {
        this.popBackStack()
    } else {
        false
    }
}