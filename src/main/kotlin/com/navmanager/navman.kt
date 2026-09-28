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

    private val _backStack: MutableStateFlow<List<Route<P>>> = MutableStateFlow(emptyList())
    val backStack: StateFlow<List<Route<P>>> = _backStack.asStateFlow()

    fun goTo(path: String): Boolean {
        val resolvedRoute = matcher.match(path)
        if (resolvedRoute.routeNode == null) return false
        if (resolvedRoute == currentRoute.value) return false

        _backStack.value += _currentRoute.value
        _currentRoute.value = resolvedRoute
        _canGoBack.value = _backStack.value.isNotEmpty()
        return true
    }

    fun goTo(route: Route<P>) {
        if (route == currentRoute.value) return

        _backStack.value += _currentRoute.value
        _currentRoute.value = route
        _canGoBack.value = _backStack.value.isNotEmpty()
    }

    fun goBack(): Boolean {

        val currentBackStack = _backStack.value
        val lastRoute = currentBackStack.lastOrNull() ?: return false

        _backStack.value = currentBackStack.dropLast(1)
        _currentRoute.value = lastRoute
        _canGoBack.value = _backStack.value.isNotEmpty()
        return true
    }
}

