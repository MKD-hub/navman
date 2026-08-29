package com.navmanager

class NavMan(var currentRoute: String) {

    var prevRoutes: MutableList<String> = mutableListOf()

    fun goTo(routeName: String) {
        prevRoutes.add(currentRoute)
        currentRoute = routeName
    }

    fun goBack(): Boolean {
        val previousRoute = prevRoutes.removeLastOrNull() ?: return false
        currentRoute = previousRoute
        return true
    }
}
