package com.codingpizza.euterpe.ui.navigation

sealed interface Screens<T> {

    fun <T> generateRoute(route: String, paramKey: String, param: T): String = route.replace("{$paramKey}", "$param")

    object Player : Screens<String>

    object ItemDetail : Screens<Int>
}
