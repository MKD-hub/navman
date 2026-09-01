package com.navmanager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavMan(initialRoute: String) {

    // having this in state flow essentially makes currentRoute observable to another thread
    private val _currentRoute = MutableStateFlow(initialRoute)
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()

    var prevRoutes: MutableList<String> = mutableListOf()

    fun goTo(routeName: String) {
        prevRoutes.add(_currentRoute.value)
        _currentRoute.value = routeName
    }

    fun goBack(): Boolean {
        val lastRoute = prevRoutes.removeLastOrNull() ?: return false
        _currentRoute.value = lastRoute
        return true
    }
}
