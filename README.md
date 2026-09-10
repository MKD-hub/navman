# NavMan

A lightweight, zero-dependency, Trie-based navigation and route-matching engine written in pure Kotlin.

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF.svg?style=flat&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Tests](https://img.shields.io/badge/Tests-21%20passing-brightgreen.svg)]()
[![Platform](https://img.shields.io/badge/Platform-JVM%20%7C%20Android%20%7C%20KMP-blue.svg)]()
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

navman provides high-performance URL pattern matching and reactive navigation state management without any coupling to Jetpack Compose, Android, or third-party frameworks. It can be used anywhere: desktop, Android, backend (Ktor), or Kotlin Multiplatform.

---

## Features

- **Trie-Powered Resolution**: O(L) lookup performance where L is the depth of URL segments, completely independent of the number of registered routes.
- **Static-First Precedence**: Exact static paths (e.g. `/home/admin/overview`) always take priority over dynamic parameter wildcards (e.g. `/home/admin/:id`).
- **Order-Independent Registration**: Register deep routes before or after shallow routes. Intermediate container nodes are automatically upgraded into endpoints without state conflicts.
- **Path and Query Parameter Binding**: Automatically extracts dynamic path tokens (`:id`, `:postId`) and decodes query strings (`?sort=desc&active`), merging them into a unified `params` map.
- **Reactive StateFlow History**: Built-in LIFO back-stack controller (`NavMan`) exposing `StateFlow<String>` for immediate thread-safe UI binding.
- **Duplicate Protection**: Throws `DuplicateValueException` when registering identical endpoint patterns.
- **ASCII Tree Visualization**: Inspect your route hierarchy in your terminal or tests using `routeTree.prettyPrint()`.

---

## Quickstart

### 1. Define Routes with RouteBuilder

Use the generic `RouteBuilder<P>` to register patterns and bind generic handlers (lambdas, screen composables, or metadata objects):

```kotlin
import com.navmanager.RouteBuilder

// Register routes with handler lambdas:
val builder = RouteBuilder<(Map<String, String>) -> Unit>()
    .addRoute("/home") { params ->
        println("Navigated to Home")
    }
    .addRoute("/home/admin/:id") { params ->
        println("Admin ID: ${params["id"]}")
    }
    .addRoute("/home/admin/overview") { params ->
        println("Admin Overview (Static Priority)")
    }

val matcher = builder.build()
```

---

### 2. Match URLs with RouteMatcher

Pass raw or normalized URLs into `matcher.match()`:

```kotlin
import com.navmanager.RouteParser

val parser = RouteParser()
val normalized = parser.normalize("/home/admin/108?sort=desc&active")

val result = matcher.match(normalized)

if (result.routeNode != null) {
    println("Pattern: ${result.routeNode.routePattern}") // "/home/admin/:id/"
    println("Extracted Params: ${result.params}")         // {"id": "108", "sort": "desc", "active": "true"}

    // Invoke matched handler:
    result.routeNode.handler?.invoke(result.params)
} else {
    println("404: Route Not Found")
}
```

---

### 3. Reactive Backstack Navigation with NavMan

Manage forward navigation and back-stack history with `NavMan`:

```kotlin
import com.navmanager.NavMan
import kotlinx.coroutines.flow.collect

val nav = NavMan(initialRoute = "/home")

// Collect route changes reactively:
// In Jetpack Compose: val currentRoute by nav.currentRoute.collectAsState()
coroutineScope.launch {
    nav.currentRoute.collect { route ->
        println("Current Screen: $route")
    }
}

nav.navigate("/articles/42")
nav.navigate("/settings")

nav.goBack() // Returns true, currentRoute emits "/articles/42"
nav.goBack() // Returns true, currentRoute emits "/home"
nav.goBack() // Returns false (cannot pop root route)
```

---

### 4. Inspect the Route Hierarchy

Call `prettyPrint()` to visualize your compiled Trie in the terminal:

```kotlin
println(builder.rt.prettyPrint())
```

Output:
```text
├── / [✓ / -> null]
│   ├── home [✓ /home/ -> Handler]
│   │   ├── admin
│   │   │   ├── overview [✓ /home/admin/overview/ -> Handler]
│   │   │   ├── :id [✓ /home/admin/:id/ -> Handler]
```

---

## Architecture

```
Raw URL: "/home/admin/108?sort=desc"
                 │
                 ▼
       [ RouteParser.normalize() ]
         ├── Path:  "/home/admin/108"
         └── Query: "sort=desc"
                 │
                 ▼
       [ RouteMatcher.match() ]
         │
         ├── 1. Static Child Check ("/home" -> "admin")
         ├── 2. Fallback to Param Child (":id") -> Extracts {"id": "108"}
         └── 3. Decode Query String -> Extracts {"sort": "desc"}
                 │
                 ▼
       Returns Route<P>(
           name = "/home/admin/108/",
           params = {"id": "108", "sort": "desc"},
           routeNode = RouteNode(handler = P, routePattern = "/home/admin/:id/")
       )
```

---

## Testing

Run the test suite with Gradle:

```bash
./gradlew test
```

All 21 unit tests verify:
- Iterative pointer walks and Trie insertions
- Static route priority over dynamic parameters
- Out-of-order route declarations and container node upgrading
- Dynamic path parameter and multi-query decoding
- Duplicate endpoint detection and 404 container protection
- Back-stack LIFO history popping and reactive StateFlow emission

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
