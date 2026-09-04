package com.navmanager

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

// TestRouteBuilder: Unit test suite for RouteBuilder initialization and validation
// TODO: Implement RouteBuilder test cases

class TestRouteBuilder {
    @Test
    fun `test build immutable trie`() {
        val rb = RouteBuilder<String>()
            .addRoute("/home", "h")
            .addRoute("/home/admin/builder", "o")
            .addRoute("/home/admin/builder/:id", "m")
            .build()

        assertNotNull(rb)
    }

    @Test
    fun `should fail on duplicate route`() {
        val rb = RouteBuilder<String>()
        val error = assertFailsWith<DuplicateValueException> {
            rb
                .addRoute("/home/admin/builder/:id/posts", "f")
                .addRoute("/home/admin/builder/:id/posts/:postId", "r")
                .addRoute("/home/admin/builder/:id", "m")
                .addRoute("/home", "h")
                .addRoute("/home/admin/builder", "o")
                .addRoute("/home", "h")

        }
        assertEquals("duplicate route not allowed", error.message)
    }
}
