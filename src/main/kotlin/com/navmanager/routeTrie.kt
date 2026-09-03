package com.navmanager

data class RouteNode(
    val segmentName: String,
    val staticChildren: MutableMap<String, RouteNode> = mutableMapOf(),
    var paramChild: RouteNode?,
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

    fun getNode(node: RouteNode? = rootNode, routeSegment: String): RouteNode? {
        val route = rp.normalize(routeSegment)
        val paths = route.routeName.split("/").filterNot { it.isBlank() }

        var current: RouteNode? = rootNode

        for (segment in paths) {
            val paramNode = current?.paramChild
            current = when {
                segment.startsWith(":") -> {
                    if (paramNode != null && paramNode.segmentName == segment) {
                        paramNode
                    } else {
                        return null
                    }
                }
                current?.staticChildren?.containsKey(segment) == true -> {
                    current.staticChildren[segment]
                }
                else -> return null
            }
        }

        return current
    }

    fun insert(path: String): RouteNode {
        val route: NormalizedRouteParamPair = rp.normalize(path)
        val paths = route.routeName.split("/").filterNot { it.isBlank() }

        var current = rootNode

        for (segment in paths) {
            val isLastSegment = (segment == paths.last())
            when {
                segment.startsWith(":") -> {
                    if (current.paramChild == null) {
                        current.paramChild = RouteNode(
                            segmentName = segment,
                            staticChildren = mutableMapOf(),
                            paramChild = null,
                            isEndOfRoute = isLastSegment,
                            routePattern = if (isLastSegment) route.routeName else null
                        )
                    }
                    current = current.paramChild!!
                }
                else -> {
                    if (current.staticChildren[segment] == null) {
                        current.staticChildren[segment] = RouteNode(
                            segmentName = segment,
                            staticChildren = mutableMapOf(),
                            paramChild = null,
                            isEndOfRoute = isLastSegment,
                            routePattern = if (isLastSegment) route.routeName else null
                        )
                    }
                    current = current.staticChildren[segment]!!
                }
            }
        }

        return current
    }
}
