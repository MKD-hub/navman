package com.navmanager

import kotlin.test.Test
import kotlin.test.assertNotNull

// what behaviour do we want when there is a match?
// matcher should getNode to find the route with its handler
// it should also pass params to it
//
// so... I am checking that the node is retreived
// I am checking that the handler is being called with the correct params passed to it
// How do I check that a generic function is being called with X argument?
//

class TestMatcher {
  @Test
  fun `should call hanlder with correct params`() {
    val parser = RouteParser()
    var capturedParams: Map<String, String>? = null

    val builder =
            RouteBuilder<(Map<String, String>) -> Unit>().addRoute("/home/admin/:id") { params ->
              capturedParams = params
            }

    val matcher = RouteMatcher(builder.rt)
    val route = parser.normalize("/home/admin/123?admin=true")
    println(route)
    val routeNodeAndParams = matcher.match(route)
    assertNotNull(routeNodeAndParams, "this shit wasn't supposed to be null, bitch.")
  }
}
