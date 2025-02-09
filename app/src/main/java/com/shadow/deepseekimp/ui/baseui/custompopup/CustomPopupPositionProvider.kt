package com.shadow.deepseekimp.ui.baseui.custompopup

import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider

@Immutable
data class CustomPopupPositionProvider(
    val contentOffset: DpOffset,
    val density: Density,
    val onPopupPositionFound: (Alignment.Horizontal, Boolean) -> Unit
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val contentOffsetX = with(density) { contentOffset.x.roundToPx() }
        val contentOffsetY = with(density) { contentOffset.y.roundToPx() }

        val isFitEnd =
            (anchorBounds.left + contentOffsetX + popupContentSize.width) < windowSize.width
        val isFitStart = (anchorBounds.left - contentOffsetX - popupContentSize.width) > 0
        val popupHalfWidth = popupContentSize.width / 2
        val halfAnchor = (anchorBounds.right - anchorBounds.left) / 2
        val isFitCenter =
            ((anchorBounds.left + halfAnchor + popupHalfWidth) < windowSize.width) &&
                    ((anchorBounds.left + halfAnchor - popupHalfWidth) > 0)

        val endPlacementOffset = anchorBounds.left - contentOffsetX
        val centerPlacementOffset = anchorBounds.left - popupHalfWidth + contentOffsetX
        val startPlacementOffset = anchorBounds.right + contentOffsetX - popupContentSize.width

        val bottomCoordinatesY = anchorBounds.bottom + popupContentSize.height
        val isFitBottom = bottomCoordinatesY <= windowSize.height
        val topCoordinatesY = anchorBounds.top - popupContentSize.height
        val isFitTop = topCoordinatesY > 0 || anchorBounds.top > windowSize.height

        // Вычислить вертикальное положение.
        val toBottom = anchorBounds.bottom + contentOffsetY
        val toTop = anchorBounds.top - contentOffsetY - popupContentSize.height
        val toCenter = anchorBounds.top - popupContentSize.height / 2
        val toDisplayBottom = windowSize.height - popupContentSize.height
        val yOffset = sequenceOf(
            if (isFitBottom) toBottom else toTop,
            toCenter,
            toDisplayBottom
        ).firstOrNull {
            it + popupContentSize.height <= windowSize.height
        } ?: toBottom

        val horizontalAndOffset = getHorizontalOffset(
            isFitsStart = isFitStart,
            isFitsEnd = isFitEnd,
            isFitsCenter = isFitCenter,
            endPlacementOffset = endPlacementOffset,
            startPlacementOffset = startPlacementOffset,
            centerPlacementOffset = centerPlacementOffset
        )

        onPopupPositionFound(horizontalAndOffset.first, isFitTop)
        return IntOffset(horizontalAndOffset.second, yOffset)
    }
    private fun getHorizontalOffset(
        isFitsStart: Boolean,
        isFitsEnd: Boolean,
        isFitsCenter: Boolean,
        endPlacementOffset: Int,
        startPlacementOffset: Int,
        centerPlacementOffset: Int
    ): Pair<Alignment.Horizontal, Int> {

        // Check which alignment fits the best.
        val alignments = listOf(
            Alignment.Start,
            Alignment.CenterHorizontally,
            Alignment.End
        )

        // Check the corresponding offsets.
        val offsets = listOf(
            endPlacementOffset,
            centerPlacementOffset,
            startPlacementOffset
        )

        // Check which alignment and offset fits the best.
        val fallbacks = mutableListOf<Pair<Alignment.Horizontal, Int>>()
        for (index in 0..2) {
            if (listOf(isFitsEnd, isFitsCenter, isFitsStart)[index]) {
                fallbacks.add(Pair(alignments[index], offsets[index]))
            }
        }

        // If there is a fallback, choose it as the alignment and offset.
        val fallback = fallbacks.firstOrNull()
        if (fallback != null) {
            return fallback
        }

        // If there is no fallback, calculate the horizontal offset.
        val finalHorizontalFallback = if (isFitsStart) {
            0
        } else if (isFitsEnd) {
            2
        } else {
            1
        }
        val fallbackHorizontalOffset = offsets[finalHorizontalFallback]
        return Pair(alignments[finalHorizontalFallback], fallbackHorizontalOffset)
    }
}