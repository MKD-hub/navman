package com.navmanager

data class Route(
    val name: String,
    val params: Map<String, String> = emptyMap()
)

data class NormalizedRouteParamPair(
    val routeName: String,
    val params: String?
)

class RouteMatcher<P>(tree: RouteTree<P>) {
    fun match(normalizedRoute: NormalizedRouteParamPair): Route {

        //TODO: implement route matching
        return Route(
            name = "/home/feeds/"
        )
    }
}
