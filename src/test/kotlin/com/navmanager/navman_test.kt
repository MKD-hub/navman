package com.navmanager

import kotlin.test.Test
import kotlin.test.assertEquals

class NavmanTest {
    private val builder = RouteBuilder<String>()
        .addRoute("/", "rootHandler")
        .addRoute("/home", "homeHandler")
        .addRoute("/feeds", "feedsHandler")
        .addRoute("/articles/:id", "articleHandler")

    private val matcher = builder.build()

    @Test
    fun `navman initialization`() {
        val nm = NavMan(matcher = matcher, initialPath = "/home")
        assertEquals("/home/", nm.currentRoute.value.name)
        assertEquals("homeHandler", nm.currentRoute.value.routeNode?.handler)
    }

    @Test
    fun `navman change route with params`() {
        val nm = NavMan(matcher = matcher, initialPath = "/home")
        val success = nm.goTo("/articles/42?sort=desc")

        assertEquals(true, success)
        assertEquals("/articles/:id/", nm.currentRoute.value.routeNode?.routePattern)
        assertEquals("42", nm.currentRoute.value.params["id"])
        assertEquals("desc", nm.currentRoute.value.params["sort"])
        assertEquals("articleHandler", nm.currentRoute.value.routeNode?.handler)
    }

    @Test
    fun `navman go back`() {
        val nm = NavMan(matcher = matcher, initialPath = "/")
        nm.goTo("/home")
        nm.goTo("/feeds")
        assertEquals(true, nm.goBack())
        assertEquals("/home/", nm.currentRoute.value.name)
        assertEquals(true, nm.goBack())
        assertEquals("/", nm.currentRoute.value.name)
    }

    @Test
    fun `navman go back error on empty history`() {
        val nm = NavMan(matcher = matcher, initialPath = "/")
        assertEquals(false, nm.goBack())
        assertEquals(false, nm.goBack())
        assertEquals("/", nm.currentRoute.value.name)
    }

    @Test
    fun `navman goTo unmatched route returns false and keeps current route`() {
        val nm = NavMan(matcher = matcher, initialPath = "/home")
        val success = nm.goTo("/nonexistent/unknown")
        assertEquals(false, success)
        assertEquals("/home/", nm.currentRoute.value.name)
    }
}

