package com.navmanager

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class TestRouteTrie {
    val rt = RouteTree()

    @Test
    fun `test insert`() {
        // creates a route prefix tree with a base route of "/"
        assertNotNull(rt.insert("/home/admin/:id"))
        assertNotNull(rt.rootNode.staticChildren["home"])
        assertNotNull(rt.rootNode.staticChildren["home"]?.staticChildren["admin"])
        assertNotNull(rt.rootNode.staticChildren["home"]?.staticChildren["admin"])
        assertNotNull(rt.rootNode.staticChildren["home"]?.staticChildren["admin"]?.paramChild?.segmentName)
        assertEquals(rt.rootNode.staticChildren["home"]?.staticChildren["admin"]?.paramChild?.segmentName, ":id")

        assertNotNull(rt.insert("/home/admin/:id/posts/:postId"))
        assertNotNull(rt.rootNode.staticChildren["home"])
        assertNotNull(rt.rootNode.staticChildren["home"]?.staticChildren["admin"])
        assertNotNull(rt.rootNode.staticChildren["home"]?.staticChildren["admin"])
        assertNotNull(rt.rootNode.staticChildren["home"]?.staticChildren["admin"]?.paramChild?.segmentName)
        assertEquals(rt.rootNode.staticChildren["home"]?.staticChildren["admin"]?.paramChild?.segmentName, ":id")
        assertEquals(
            rt.rootNode.staticChildren["home"]?.staticChildren["admin"]?.paramChild?.staticChildren["posts"]?.paramChild?.segmentName,
            ":postId"
        )

    }

    @Test
    fun `get node from tree`() {
        val rt = RouteTree()
        rt.insert("/home/admin/:id")
        assertNotNull(rt.getNode(routeSegment = "/home"), "for input /home")
        assertNotNull(rt.getNode(routeSegment = "/home/admin"), "for input /home/admin")
        assertNotNull(rt.getNode(routeSegment = "/home/admin/:id"), "for input /home/admin/:id")
    }

}
