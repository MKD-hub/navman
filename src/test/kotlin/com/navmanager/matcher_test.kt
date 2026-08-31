package com.navmanager

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFails

class TestNormalize {
    @Test
    fun `can normalize route strings`() {
        val rs = RouteParser()
        val routeExample = "home/feeds"
        val normalizedRoute = rs.normalize(routeExample)

        assertEquals("/home/feeds/", normalizedRoute)
    }

    @Test
    fun `can normalize route parameters that have multiple forward slashes`() {
        val rs = RouteParser()
        val routeExample = "////home//feeds/"
        val normalizedRoute = rs.normalize(routeExample)
        assertEquals("/home/feeds/", normalizedRoute)
    }

    @Test
    fun `retuns an error when query params are broken`() {
        assertFails {
            val rs = RouteParser()
            val routeExample = "/home/feeds?"
            val normalizedRoute = rs.normalize(routeExample)
        }
    }
}
