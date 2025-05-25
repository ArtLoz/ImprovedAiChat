@file:Suppress("UNCHECKED_CAST")

package com.shadow.deepseekimp.ui.baseui

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

inline fun ViewModel.io(crossinline suspender: suspend () -> Unit): Job {
    return viewModelScope.launch(Dispatchers.IO) {
        suspender()
        Int
    }
}

fun LazyListState.isScrolledToTheEnd(): Boolean {
    val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastItem == null || lastItem.size + lastItem.offset <= layoutInfo.viewportEndOffset
}

suspend fun LazyListState.scrollToEnd() {
    val itmIndex = this.layoutInfo.totalItemsCount - 1
    if (itmIndex >= 0) {
        val lastItem = this.layoutInfo.visibleItemsInfo.lastOrNull()
        lastItem?.let {
            this.animateScrollToItem(itmIndex, it.size + it.offset)
        }
    }
}

fun fadeOut400delay(): ExitTransition {
    return fadeOut(tween(400))
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}


@Composable
fun <T : Any> LazyListState.rememberLastVisibleItemKey(keyClass: KClass<T>): State<T?> {
    return remember {
        derivedStateOf {
            val key = this.layoutInfo.visibleItemsInfo.lastOrNull()?.key
            keyClass.safeCast(key) // Безопасное приведение
        }
    }
}
fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

fun PagerState.startOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtLeast(0f)
}

fun PagerState.endOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtMost(0f)
}
