package com.navmanager

data class Route<P>(
        val name: String,
        val params: Map<String, String> = emptyMap(),
        val routeNode: RouteNode<P>?
)

data class NormalizedRouteParamPair(val routeName: String, var params: String?)

class RouteMatcher<P>(val tree: RouteTree<P>) {
  fun match(normalizedRoute: NormalizedRouteParamPair): Route<P> {

    val route = normalizedRoute.routeName.split("/").filterNot { it.isBlank() }
    var current: RouteNode<P>? = tree.rootNode
    val rs = RouteParser()
    var paramsMap = rs.parseQuery(normalizedRoute.params).toMutableMap()
    var prevRoute = current

    for (segment in route) {
      if (current == null) break
      current =
              when {
                current.staticChildren.containsKey(segment) -> {
                  prevRoute = current.staticChildren[segment]
                  current.staticChildren[segment]!!
                }
                else -> {
                  if (prevRoute?.paramChild != null) {
                    val child = prevRoute.paramChild
                    paramsMap.put(child?.segmentName?.substring(1)!!, segment)
                    prevRoute = child
                    child
                  } else {
                    null
                  }
                }
              }
    }

    if (current != null && !current.isEndOfRoute) {
      current = null
    }

    return Route(name = normalizedRoute.routeName, params = paramsMap, routeNode = current)
  }

  fun match(path: String): Route<P> {
    val rs = RouteParser()
    return match(rs.normalize(path))
  }
}
