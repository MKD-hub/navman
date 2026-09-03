package com.navmanager

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class TestRouteTrie {
    val rt = RouteTree<String>()

    @Test
    fun `test insert`() {
        // creates a route prefix tree with a base route of "/"
        assertNotNull(rt.insert("/home/admin/", "don't"))
        assertNotNull(rt.insert("/home/admin/:id", "look"))
        assertNotNull(rt.rootNode.staticChildren["home"])
        assertNotNull(rt.rootNode.staticChildren["home"]?.staticChildren["admin"])
        assertNotNull(rt.rootNode.staticChildren["home"]?.staticChildren["admin"])
        assertNotNull(rt.rootNode.staticChildren["home"]?.staticChildren["admin"]?.paramChild?.segmentName)
        assertEquals(rt.rootNode.staticChildren["home"]?.staticChildren["admin"]?.paramChild?.segmentName, ":id")

        assertNotNull(rt.insert("/home/admin/:id/posts/:postId", "back"))

        assertNotNull(rt.rootNode.staticChildren["home"])
        assertNotNull(rt.rootNode.staticChildren["home"]?.staticChildren["admin"])
        assertNotNull(rt.rootNode.staticChildren["home"]?.staticChildren["admin"])
        assertNotNull(rt.rootNode.staticChildren["home"]?.staticChildren["admin"]?.paramChild?.segmentName)
        assertEquals(rt.rootNode.staticChildren["home"]?.staticChildren["admin"]?.paramChild?.segmentName, ":id")
        assertEquals(
            rt.rootNode.staticChildren["home"]?.staticChildren["admin"]?.paramChild?.staticChildren["posts"]?.paramChild?.handler,
            "back"
        )

    }

    @Test
    fun `get node from tree`() {
        val rt = RouteTree<String>()
        rt.insert("/home/admin/:id", "hey")
        assertNotNull(rt.getNode(routeSegment = "/"), "for input /")
        assertNotNull(rt.getNode(routeSegment = "/home"), "for input /home")
        assertNotNull(rt.getNode(routeSegment = "/home/admin"), "for input /home/admin")
        assertNotNull(rt.getNode(routeSegment = "/home/admin/:id"), "for input /home/admin/:id")
    }

}
