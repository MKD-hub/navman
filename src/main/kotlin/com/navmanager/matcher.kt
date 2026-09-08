package com.navmanager

data class Route(val name: String, val params: Map<String, String> = emptyMap())

data class NormalizedRouteParamPair(val routeName: String, val params: String?)

class RouteMatcher<P>(tree: RouteTree<P>) {
  fun match(normalizedRoute: NormalizedRouteParamPair): Route {
    // first look for :id... how do I know if something is supposed to be matched as :id?

    // TODO: implement route matching
    return Route(name = "/home/feeds/")
  }
}
