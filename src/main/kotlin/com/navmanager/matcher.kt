package com.navmanager

data class Route(
    val name: String,
    val params: Map<String, String> = emptyMap()
)

class RouteParser {
    fun normalize(routeString: String): String {
        val route = routeString.trim()
        val normalizedStringList = route.split("/").filterNot { it == "" }
        val params = routeString.split("?")

        val normalizedString = normalizedStringList.joinToString(prefix = "/", separator = "/", postfix = "/")

        if (params.last() == "") {
            throw IllegalArgumentException("cannot parse empty query params")
        }
        return normalizedString
    }

    fun parse(routeString: String): Route {

        return Route(
            name = "home"
        )
    }
}
