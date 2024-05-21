package com.gabcode.compassabout.ui

enum class NavigatorRoute {
    HOME, ABOUT
}
interface Navigator {
    fun navigate(route: NavigatorRoute)
}
