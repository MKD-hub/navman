package com.navmanager

class DuplicateValueException(
    message: String,
    val duplicateValue: Any? = null
) : Exception(message)

// RouteBuilder: Constructs and validates an immutable RouteTree and RouteMatcher
class RouteBuilder<P> {
    private val routes: MutableMap<String, P> = mutableMapOf()
    val rt: RouteTree<P> = RouteTree<P>()
    private val rp: RouteParser = RouteParser()

    fun addRoute(pattern: String, handler: P): RouteBuilder<P> {
        val route: NormalizedRouteParamPair = rp.normalize(pattern)
        rt.insert(route.routeName, handler)
        return this
    }

    fun build(): RouteMatcher<P> {
        return RouteMatcher(tree = rt)
    }
}
