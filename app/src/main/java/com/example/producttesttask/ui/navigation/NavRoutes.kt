package com.example.producttesttask.ui.navigation

sealed class NavRoutes(val route: String) {
    object MainScreen : NavRoutes("main")
    object ProductDetailScreen : NavRoutes("product")

    fun withArgs(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    fun withArgs(vararg args: Int): String{
        return buildString {
            append(route)
            args.forEach {arg ->
                append("/$arg")
            }
        }
    }
}