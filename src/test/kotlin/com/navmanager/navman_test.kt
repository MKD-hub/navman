package com.navmanager

import kotlin.test.Test
import kotlin.test.assertEquals

class NavmanTest {

    @Test
    fun `navman initialization`() {
        val nm = NavMan(currentRoute = "Home")
        assertEquals("Home", nm.currentRoute)
    }

    @Test
    fun `navman change route`() {
        val nm = NavMan(currentRoute = "Home")
        nm.goTo("feeds")

        assertEquals("feeds", nm.currentRoute, "expected feeds, got ${nm.currentRoute}")
    }

    @Test
    fun `navman go back`() {
        val nm = NavMan(currentRoute = "/")
        nm.goTo("home")
        nm.goTo("feeds")
        nm.goBack()
        nm.goBack()

        assertEquals("/", nm.currentRoute, "expected home, got ${nm.currentRoute}")
    }

    @Test
    fun `navman go back error`() {
        val nm = NavMan(currentRoute = "/")
        nm.goTo("home")
        nm.goTo("feeds")
        nm.goBack()
        nm.goBack()
        nm.goBack()

        assertEquals("/", nm.currentRoute, "expected \"/\", got ${nm.currentRoute}")
    }
}
