package com.navmanager

class RouteParser {
  fun normalize(routeString: String): NormalizedRouteParamPair {
    // val route = routeString.trim()
    val parts = routeString.split("?", limit = 2)
    val normalizedStringList = parts[0].split("/").filterNot { it.isBlank() }

    val normalizedString =
            if (normalizedStringList.isEmpty()) "/"
            else normalizedStringList.joinToString(prefix = "/", separator = "/", postfix = "/")

    val queryPart =
            if (parts.size > 1) {
              if (parts[1].isBlank()) {
                throw IllegalArgumentException("cannot parse empty query params")
              }
              parts[1]
            } else null

    return NormalizedRouteParamPair(routeName = normalizedString, params = queryPart)
  }

  fun parseQuery(queryParams: String?): Map<String, String> {
    if (queryParams.isNullOrBlank()) return emptyMap()
    val queries = queryParams.split("&")
    val qMap =
            queries.associate { query ->
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
}
