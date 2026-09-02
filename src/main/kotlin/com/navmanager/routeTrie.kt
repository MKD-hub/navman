package com.navmanager

data class RouteNode(
    val segmentName: String,
    val staticChildren: MutableMap<String, RouteNode> = mutableMapOf(),
    val paramChild: RouteNode?,
    val isEndOfRoute: Boolean,
    val routePattern: String?
)

fun routeExists(n: RouteNode, searchTerm: String): Boolean {
    return n.staticChildren[searchTerm] == null
}

class RouteTree {
    val rp = RouteParser()

    val rootNode = RouteNode(
        segmentName = "/",
        staticChildren = mutableMapOf(),
        paramChild = null,
        isEndOfRoute = true,
        routePattern = "/"
    )

    // fun getNode(node: RouteNode? = rootNode, routeSegment: String): RouteNode? {
    //     val route = rp.normalize(routeSegment)
    //     val paths = route.routeName.split("/") as MutableList
    //
    // }

    fun insert(path: String): RouteNode {
        val route: NormalizedRouteParamPair = rp.normalize(path)
        val paths = route.routeName.split("/").filterNot { it.isBlank() }

        var current = rootNode

        for (segment in paths) {
            if (current.staticChildren[segment] == null) {
                val route = RouteNode(
                    segmentName = segment,
                    staticChildren = mutableMapOf(),
                    paramChild = null,
                    isEndOfRoute = segment == paths.last(),
                    routePattern = ""
                )

                current.staticChildren[segment] = route
            }

            current = current.staticChildren[segment]!!
        }

        return current
    }
}
