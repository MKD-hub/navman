package com.navmanager

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class TestRouteTrie {
    // @Test
    // fun `get node from tree`() {
    //     val rt = RouteTree()
    //     assertNotNull(rt.getNode(routeSegment = "/"))
    // }

    @Test
    fun `test insert`() {
        // creates a route prefix tree with a base route of "/"
        val rt = RouteTree()
        assertNotNull(rt.insert("/home/admin"))
        assertNotNull(rt.rootNode.staticChildren["home"])
        assertNotNull(rt.rootNode.staticChildren["home"]?.staticChildren["admin"])
    }
}
