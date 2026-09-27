package com.navmanager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavMan<P>(
    val matcher: RouteMatcher<P>,
    initialPath: String
) {
    private val _canGoBack: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _currentRoute: MutableStateFlow<Route<P>> =
        MutableStateFlow(matcher.match(initialPath))
    val currentRoute: StateFlow<Route<P>> = _currentRoute.asStateFlow()
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    private val prevRoutes: MutableList<Route<P>> = mutableListOf()

    fun goTo(path: String): Boolean {
        val resolvedRoute = matcher.match(path)
        if (resolvedRoute.routeNode == null) return false

        prevRoutes.add(_currentRoute.value)
        _currentRoute.value = resolvedRoute
        _canGoBack.value = prevRoutes.isNotEmpty()
        return true
    }

    fun goTo(route: Route<P>) {
        prevRoutes.add(_currentRoute.value)
        _currentRoute.value = route
        _canGoBack.value = prevRoutes.isNotEmpty()
    }

    fun goBack(): Boolean {
        val lastRoute = prevRoutes.removeLastOrNull() ?: return false
        _currentRoute.value = lastRoute
        _canGoBack.value = prevRoutes.isNotEmpty()
        return true
    }
}

