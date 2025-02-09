package com.app.trainr.ui.nav

import android.util.Log

fun checkClassRoute(currentRoute: String?, route: Any): Boolean {
    return currentRoute?.contains(route::class.qualifiedName.toString().replace(".Companion", ""))
        ?: false
}
fun checkClassRoute(currentRoute: String?, route: String): Boolean {
    return currentRoute?.contains(route.replace(".Companion", "")) ?: false
}