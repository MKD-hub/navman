package com.navmanager

data class RouteNode<P>(
    val segmentName: String,
    val staticChildren: MutableMap<String, RouteNode<P>> = mutableMapOf(),
    var paramChild: RouteNode<P>? = null,
    var isEndOfRoute: Boolean,
    var routePattern: String?,
    var handler: P? = null
) {
    fun prettyPrint(indent: String = ""): String {
        val sb = StringBuilder()
        val endLabel = if (isEndOfRoute) " [✓ $routePattern -> $handler]" else ""
        sb.append("$indent├── $segmentName$endLabel\n")

        for ((_, child) in staticChildren) {
            sb.append(child.prettyPrint("$indent│   "))
        }
        paramChild?.let { child ->
            sb.append(child.prettyPrint("$indent│   "))
        }
        return sb.toString()
    }
}

class RouteTree<P> {
    val rp = RouteParser()

    val rootNode = RouteNode<P>(
        segmentName = "/",
        staticChildren = mutableMapOf(),
        paramChild = null,
        isEndOfRoute = true,
        routePattern = "/",
        handler = null
    )

    fun getNode(node: RouteNode<P>? = rootNode, routeSegment: String): RouteNode<P>? {
        val route = rp.normalize(routeSegment)
        val paths = route.routeName.split("/").filterNot { it.isBlank() }

        var current: RouteNode<P>? = rootNode

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

    fun insert(path: String, handler: P): RouteNode<P> {
        val paths = path.split("/").filterNot { it.isBlank() }

        var current = rootNode

        for (segment in paths) {
            when {
                segment.startsWith(":") -> {
                    if (current.paramChild == null) {
                        current.paramChild = RouteNode(
                            segmentName = segment,
                            staticChildren = mutableMapOf(),
                            paramChild = null,
                            isEndOfRoute = false,
                            routePattern = null,
                            handler = null
                        )
                    }
                    current = current.paramChild!!
                }

                else -> {
                    if (current.staticChildren[segment] == null) {
                        current.staticChildren[segment] = RouteNode<P>(
                            segmentName = segment,
                            staticChildren = mutableMapOf(),
                            paramChild = null,
                            isEndOfRoute = false,
                            routePattern = null,
                            handler = null
                        )
                    }
                    current = current.staticChildren[segment]!!
                }
            }
        }

        if (current.isEndOfRoute) {
            throw DuplicateValueException(
                message = "duplicate route not allowed",
                duplicateValue = path
            )
        }

        current.isEndOfRoute = true
        current.routePattern = path
        current.handler = handler

        return current
    }

    fun prettyPrint(): String = rootNode.prettyPrint()
}
