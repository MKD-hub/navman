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

        assertEquals("/home/feeds/", normalizedRoute.routeName)
    }

    @Test
    fun `can normalize route parameters that have multiple forward slashes`() {
        val rs = RouteParser()
        val routeExample = "////home//feeds/"
        val normalizedRoute = rs.normalize(routeExample)
        assertEquals("/home/feeds/", normalizedRoute.routeName)
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

class TestParsingParams {
    @Test
    fun `can extract params`() {
        val rs = RouteParser()
        val routeExample = "/home/feeds/123?page=2&sort=asc"
        val params = rs.normalize(routeExample).params
        val parsedParams = rs.parseQuery(params!!)
        assertEquals(parsedParams["page"], "2")
    }

    @Test
    fun `fails on bad input`() {
        assertFails {
            val rs = RouteParser()
            val routeExample = "/home/feeds/123?page=2&sort="
            val params = rs.normalize(routeExample).params
            val parsedParams = rs.parseQuery(params!!)
        }

        assertFails {
            val rs = RouteParser()
            val routeExample = "/home/feeds/123?=2&sort=desc"
            val params = rs.normalize(routeExample).params
            val parsedParams = rs.parseQuery(params!!)
        }
    }

    @Test
    fun `works with boolean params`() {
        val rs = RouteParser()
        val routeExample = "/home/feeds/123?active&size=1"
        val params = rs.normalize(routeExample).params
        val parsedParams = rs.parseQuery(params!!)
        assertEquals(parsedParams["active"], "true")
        assertEquals(parsedParams["size"], "1")
    }
}

class TestMatch {
    @Test
    fun `can match base routes`() {
        val rs = RouteParser()
        val routeExample = "home/feeds"
        val normalizedRoute = rs.normalize(routeExample)

        val parsedRouteObject = rs.match(normalizedRoute)
        assertEquals("/home/feeds/", parsedRouteObject.name)
    }
}
