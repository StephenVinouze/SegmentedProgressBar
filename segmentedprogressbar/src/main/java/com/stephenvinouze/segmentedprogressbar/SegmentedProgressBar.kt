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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        modifier = modifier.fillMaxWidth().clipToBounds(),
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

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithSpacing(
    viewState: SegmentedProgressBarViewState =
        SegmentedProgressBarViewState(
            segmentCount = 3,
            spacing = 10.dp,
            segmentColor = SegmentColor(
                color = Color.White
            )
        )
) {
    SegmentedProgressBar(viewState)
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithSpacingAndPositiveAngle(
    viewState: SegmentedProgressBarViewState =
        SegmentedProgressBarViewState(
            segmentCount = 3,
            spacing = 10.dp,
            angle = 45f,
            segmentColor = SegmentColor(
                color = Color.White
            )
        )
) {
    SegmentedProgressBar(viewState)
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithSpacingAndNegativeAngle(
    viewState: SegmentedProgressBarViewState =
        SegmentedProgressBarViewState(
            segmentCount = 3,
            spacing = 10.dp,
            angle = -45f,
            segmentColor = SegmentColor(
                color = Color.White
            )
        )
) {
    SegmentedProgressBar(viewState)
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithCustomColor(
    viewState: SegmentedProgressBarViewState =
        SegmentedProgressBarViewState(
            segmentCount = 3,
            spacing = 10.dp,
            angle = -45f,
            segmentColor = SegmentColor(
                color = Color.Blue
            )
        )
) {
    SegmentedProgressBar(viewState)
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithCustomColorAndAlpha(
    viewState: SegmentedProgressBarViewState =
        SegmentedProgressBarViewState(
            segmentCount = 3,
            spacing = 10.dp,
            angle = -45f,
            segmentColor = SegmentColor(
                color = Color.Blue,
                alpha = 0.3f
            )
        )
) {
    SegmentedProgressBar(viewState)
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithOneProgress(
    viewState: SegmentedProgressBarViewState =
        SegmentedProgressBarViewState(
            segmentCount = 3,
            progress = 1f,
            spacing = 10.dp,
            angle = 45f,
            segmentColor = SegmentColor(
                color = Color.White
            ),
            progressColor = SegmentColor(
                color = Color.Red
            )
        )
) {
    SegmentedProgressBar(viewState)
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithProgressInTransition(
    viewState: SegmentedProgressBarViewState =
        SegmentedProgressBarViewState(
            segmentCount = 3,
            progress = 1.3f,
            spacing = 10.dp,
            angle = 45f,
            segmentColor = SegmentColor(
                color = Color.White
            ),
            progressColor = SegmentColor(
                color = Color.Red
            )
        )
) {
    SegmentedProgressBar(viewState)
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithTwoProgress(
    viewState: SegmentedProgressBarViewState =
        SegmentedProgressBarViewState(
            segmentCount = 3,
            progress = 2f,
            spacing = 10.dp,
            angle = 45f,
            segmentColor = SegmentColor(
                color = Color.White
            ),
            progressColor = SegmentColor(
                color = Color.Red
            )
        )
) {
    SegmentedProgressBar(viewState)
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithThreeProgress(
    viewState: SegmentedProgressBarViewState =
        SegmentedProgressBarViewState(
            segmentCount = 3,
            progress = 3f,
            spacing = 10.dp,
            angle = 45f,
            segmentColor = SegmentColor(
                color = Color.White
            ),
            progressColor = SegmentColor(
                color = Color.Red
            )
        )
) {
    SegmentedProgressBar(viewState)
}