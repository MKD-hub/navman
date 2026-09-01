package com.navmanager

data class Route(
    val name: String,
    val params: Map<String, String> = emptyMap()
)

data class NormalizedRouteParamPair(
    val routeName: String,
    val params: String?
)

class RouteParser {
    fun normalize(routeString: String): NormalizedRouteParamPair {
        val route = routeString.trim()
        val params = routeString.split("?")
        val normalizedStringList = route.split("/").filterNot { it.isBlank() }

        val normalizedString = normalizedStringList.joinToString(prefix = "/", separator = "/", postfix = "/")

        if (params.last() == "") {
            throw IllegalArgumentException("cannot parse empty query params")
        }

        return NormalizedRouteParamPair(
            routeName = normalizedString,
            params = params.lastOrNull()
        )
    }

    fun parseQuery(queryParams: String): Map<String, String> {
        if (queryParams.isBlank()) return emptyMap()
        val queries = queryParams.split("&")
        val qMap = queries.associate { query ->
            val parts = query.split("=", limit = 2)
            val key = parts[0]
            val value = parts.getOrNull(1) ?: "true"

            if (key.isBlank()) {
                throw IllegalArgumentException("query param var name cannot be empty")
            }

            if (!key.isBlank() && value.isBlank()) {
                throw IllegalArgumentException("query param var name not assigned")
            }

            key to value
        }
        return qMap
    }

    fun match(normalizedRoute: NormalizedRouteParamPair): Route {

        //TODO: implement route matching
        return Route(
            name = "/home/feeds/"
        )
    }
}
