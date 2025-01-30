package com.shadow.deepseekimp.ui.nav

fun checkClassRoute(currentRoute: String?, route: Any): Boolean {
    return currentRoute?.contains(route::class.qualifiedName.toString().replace(".Companion", ""))
        ?: false
}
fun checkClassRoute(currentRoute: String?, route: String): Boolean {
    return currentRoute?.contains(route.replace(".Companion", "")) ?: false
}