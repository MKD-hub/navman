package com.navmanager

import kotlin.test.Test
import kotlin.test.assertEquals

class NavmanTest {

    @Test
    fun `navman initialization`() {
        val nm = NavMan(initialRoute = "Home")
        assertEquals("Home", nm.currentRoute.value)
    }

    @Test
    fun `navman change route`() {
        val nm = NavMan(initialRoute = "Home")
        nm.goTo("feeds")

        assertEquals("feeds", nm.currentRoute.value, "expected feeds, got ${nm.currentRoute.value}")
    }

    @Test
    fun `navman go back`() {
        val nm = NavMan(initialRoute = "/")
        nm.goTo("home")
        nm.goTo("feeds")
        nm.goBack()
        nm.goBack()

        assertEquals("/", nm.currentRoute.value, "expected home, got ${nm.currentRoute.value}")
    }

    @Test
    fun `navman go back error`() {
        val nm = NavMan(initialRoute = "/")
        nm.goBack()
        nm.goBack()
        nm.goBack()

        assertEquals("/", nm.currentRoute.value, "expected \"/\", got ${nm.currentRoute.value}")
    }
}

