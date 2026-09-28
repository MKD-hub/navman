package com.navmanager

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

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

    @Test
    fun `can go back should be true when we can go back`() {
        val nm = NavMan(matcher = matcher, initialPath = "/")
        nm.goTo("/home")
        nm.goTo("/feeds")

        assertTrue(nm.canGoBack.value)

    }

    @Test
    fun `canGoBack should always be false when at the start`() {
        val nm = NavMan(matcher = matcher, initialPath = "/")
        assertFalse(nm.canGoBack.value)
    }

    @Test
    fun `canGoBack should return false even after we remove some routes`() {
        val nm = NavMan(matcher = matcher, initialPath = "/")

        nm.goTo("/home")
        nm.goTo("/feeds")

        // feeds
        assertTrue(nm.goBack())

        // home
        assertTrue(nm.canGoBack.value)

        assertTrue(nm.goBack())

        assertEquals("/", nm.currentRoute.value.name)
        assertFalse(nm.canGoBack.value)
    }

    @Test
    fun `should not allow adding the same route you're currently on`() {
        val nm = NavMan(matcher = matcher, initialPath = "/")
        nm.goTo("/home")

        assertFalse(nm.goTo("/home"))
        assertEquals("/home/", nm.currentRoute.value.name)

        // check if we cannot go back
        // because we tried to goTo /home twice we shouldn't have /home in the prevRoutes array twice
        nm.goBack()
        assertFalse(nm.canGoBack.value)

        // this means the current route is "/" and the prevRoutes has been popped
        assertEquals(0, nm.backStack.value.size)
    }

    @Test
    fun `overloaded goTo function shouldn't allow navigating to current route`() {
        val nm = NavMan(matcher = matcher, initialPath = "/")
        nm.goTo("/home")
        nm.goTo(nm.currentRoute.value)

        assertEquals(1, nm.backStack.value.size)
    }
}

