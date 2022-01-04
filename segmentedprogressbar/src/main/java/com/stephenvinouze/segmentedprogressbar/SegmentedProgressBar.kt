package com.stephenvinouze.segmentedprogressbar

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import com.stephenvinouze.segmentedprogressbar.models.SegmentColor
import com.stephenvinouze.segmentedprogressbar.models.SegmentCoordinates
import com.stephenvinouze.segmentedprogressbar.models.SegmentedProgressBarViewState

@Composable
fun SegmentedProgressBar(
    viewState: SegmentedProgressBarViewState,
    modifier: Modifier = Modifier,
    onProgressChanged: ((Float) -> Unit)? = null,
    onProgressFinished: ((Float) -> Unit)? = null,
) {
    val computer = remember { SegmentCoordinatesComputer() }

    val animatedProgress by animateFloatAsState(
        targetValue = viewState.progress,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearEasing,
        ),
        finishedListener = onProgressFinished,
    )

    val spacing = LocalDensity.current.run { viewState.spacing.toPx() }

    if (animatedProgress.compareTo(viewState.progress) == 0) {
        onProgressChanged?.invoke(animatedProgress)
    }

    Canvas(
        modifier = modifier.fillMaxWidth(),
        onDraw = {
            (0 until viewState.segmentCount).forEach { position ->
                val segmentCoordinates = computer.segmentCoordinates(
                    position = position,
                    segmentCount = viewState.segmentCount,
                    width = size.width,
                    height = size.height,
                    spacing = spacing,
                    angle = viewState.angle
                )
                drawSegment(
                    coordinates = segmentCoordinates,
                    color = viewState.segmentColor
                )
            }

            val progressCoordinates = computer.progressCoordinates(
                progress = animatedProgress.coerceIn(0f, viewState.segmentCount.toFloat()),
                segmentCount = viewState.segmentCount,
                width = size.width,
                height = size.height,
                spacing = spacing,
                angle = viewState.angle
            )
            drawSegment(
                coordinates = progressCoordinates,
                color = viewState.progressColor
            )
        }
    )
}

private fun DrawScope.drawSegment(coordinates: SegmentCoordinates, color: SegmentColor) {
    val path = Path().apply {
        reset()
        moveTo(coordinates.topLeftX, 0f)
        lineTo(coordinates.topRightX, 0f)
        lineTo(coordinates.bottomRightX, size.height)
        lineTo(coordinates.bottomLeftX, size.height)
        close()
    }
    drawPath(
        path = path,
        color = color.color,
        alpha = color.alpha,
    )
}