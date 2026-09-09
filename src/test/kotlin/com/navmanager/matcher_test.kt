package com.navmanager

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TestMatcher {
  private val parser = RouteParser()

  @Test
  fun `should call handler with correct params`() {
    var capturedParams: Map<String, String>? = null

    val builder =
            RouteBuilder<(Map<String, String>) -> Unit>().addRoute("/home/admin/:id") { params ->
              capturedParams = params
            }

    val matcher = RouteMatcher(builder.rt)
    val route = parser.normalize("/home/admin/124")
    val routeNodeAndParams = matcher.match(route)
    assertNotNull(routeNodeAndParams.routeNode, "Route node should be found")

    routeNodeAndParams.routeNode.handler?.invoke(routeNodeAndParams.params)

    assertNotNull(capturedParams, "Handler should have been called and populated capturedParams")
    assertEquals("124", capturedParams?.get("id"))
  }

  @Test
  fun `should not match intermediate container node where isEndOfRoute is false`() {
    // Only /home/admin/:id is registered, /home/admin is NOT a registered route endpoint
    val builder = RouteBuilder<String>().addRoute("/home/admin/:id", "idHandler")

    val matcher = RouteMatcher(builder.rt)
    val route = parser.normalize("/home/admin")
    val result = matcher.match(route)

    // Expected: since /home/admin is just a folder (isEndOfRoute = false), routeNode should be null
    assertNull(result.routeNode, "Intermediate container folder should not match as a valid route")
  }

  @Test
  fun `should prioritize static child over param child`() {
    var matchedHandler = ""

    val builder =
            RouteBuilder<() -> Unit>()
                    .addRoute("/home/admin/:id") { matchedHandler = "param" }
                    .addRoute("/home/admin/overview") { matchedHandler = "static" }

    val matcher = RouteMatcher(builder.rt)
    val route = parser.normalize("/home/admin/overview")
    val result = matcher.match(route)

    assertNotNull(result.routeNode, "Static route should match")
    result.routeNode.handler?.invoke()

    assertEquals("static", matchedHandler, "Static child 'overview' should be chosen over ':id'")
  }

  @Test
  fun `should merge query parameters with path parameters`() {
    val builder = RouteBuilder<String>().addRoute("/home/admin/:id", "handler")

    val matcher = RouteMatcher(builder.rt)
    val route = parser.normalize("/home/admin/124?sort=desc&active")
    val result = matcher.match(route)

    assertNotNull(result.routeNode)
    assertEquals("124", result.params["id"])
    assertEquals("desc", result.params["sort"])
    assertEquals("true", result.params["active"])
  }

  @Test
  fun `should return null routeNode when URL path does not exist`() {
    val builder = RouteBuilder<String>().addRoute("/home/admin/:id", "handler")

    val matcher = RouteMatcher(builder.rt)
    val route = parser.normalize("/users/unknown")
    val result = matcher.match(route)

    println("does not exist: $result")
    assertNull(result.routeNode, "Non-existent path should have null routeNode")
  }

  @Test
  fun `should return null routeNode when URL is deeper than registered route`() {
    val builder = RouteBuilder<String>().addRoute("/home/admin/:id", "handler")

    val matcher = RouteMatcher(builder.rt)
    val route = parser.normalize("/home/admin/124/extra/subpage")
    val result = matcher.match(route)

    println("result $result")
    assertNull(
            result.routeNode,
            "Path deeper than registered route should fail with null routeNode"
    )
  }
}
