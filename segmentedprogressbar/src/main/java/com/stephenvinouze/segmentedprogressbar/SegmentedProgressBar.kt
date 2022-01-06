package com.stephenvinouze.segmentedprogressbar

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.animation.core.AnimationSpec
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.stephenvinouze.segmentedprogressbar.models.SegmentColor
import com.stephenvinouze.segmentedprogressbar.models.SegmentCoordinates
import kotlin.math.floor

@Composable
fun SegmentedProgressBar(
    @IntRange(from = 1) segmentCount: Int,
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0) progress: Float = 0f,
    @FloatRange(from = 0.0) spacing: Dp = 0.dp,
    @FloatRange(from = -60.0, to = 60.0) angle: Float = 0f, // Beyond 60° the bevel breaks segments by nearing horizontal plane
    segmentColor: SegmentColor = SegmentColor(),
    progressColor: SegmentColor = SegmentColor(),
    progressAnimationSpec: AnimationSpec<Float> = tween(),
    onProgressChanged: ((Float) -> Unit)? = null,
    onProgressFinished: ((Float) -> Unit)? = null,
) {
    val computer = remember { SegmentCoordinatesComputer() }
    val spacingPx = LocalDensity.current.run { spacing.toPx() }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = progressAnimationSpec,
        finishedListener = onProgressFinished,
    )

    if (animatedProgress.compareTo(progress) == 0) {
        onProgressChanged?.invoke(animatedProgress)
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .clipToBounds(),
        onDraw = {
            val progressCoordinates = computer.progressCoordinates(
                progress = animatedProgress.coerceIn(0f, segmentCount.toFloat()),
                segmentCount = segmentCount,
                width = size.width,
                height = size.height,
                spacing = spacingPx,
                angle = angle
            )

            (0 until segmentCount).forEach { position ->
                val segmentCoordinates = computer.segmentCoordinates(
                    position = position,
                    segmentCount = segmentCount,
                    width = size.width,
                    height = size.height,
                    spacing = spacingPx,
                    angle = angle
                )

                // Prevents drawing segment below progress to lighten drawing cycle and avoid bad alpha progress rendering
                if (segmentCoordinates.topRightX.compareTo(progressCoordinates.topRightX) > 0) {
                    drawSegment(
                        coordinates = segmentCoordinates,
                        color = segmentColor
                    )
                }
            }

            drawSegment(
                coordinates = progressCoordinates,
                color = progressColor
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
fun ThreeSegmentedProgressBarWithSpacing() {
    SegmentedProgressBar(
        segmentCount = 3,
        spacing = 10.dp,
        segmentColor = SegmentColor(
            color = Color.White
        )
    )
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithSpacingAndPositiveAngle() {
    SegmentedProgressBar(
        segmentCount = 3,
        spacing = 10.dp,
        angle = 45f,
        segmentColor = SegmentColor(
            color = Color.White
        )
    )
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithSpacingAndNegativeAngle() {
    SegmentedProgressBar(
        segmentCount = 3,
        spacing = 10.dp,
        angle = -45f,
        segmentColor = SegmentColor(
            color = Color.White
        )
    )
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithCustomColor() {
    SegmentedProgressBar(
        segmentCount = 3,
        spacing = 10.dp,
        angle = -45f,
        segmentColor = SegmentColor(
            color = Color.Blue
        )
    )
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithCustomColorAndAlpha() {
    SegmentedProgressBar(
        segmentCount = 3,
        spacing = 10.dp,
        angle = -45f,
        segmentColor = SegmentColor(
            color = Color.Blue,
            alpha = 0.3f
        )
    )
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithOneProgress() {
    SegmentedProgressBar(
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
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithProgressInTransition() {
    SegmentedProgressBar(
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
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithTwoProgress() {
    SegmentedProgressBar(
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
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithThreeProgress() {
    SegmentedProgressBar(
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
}

@Preview(widthDp = 300, heightDp = 30)
@Composable
fun ThreeSegmentedProgressBarWithHiddenSegmentBehindProgress() {
    SegmentedProgressBar(
        segmentCount = 3,
        progress = 2f,
        spacing = 10.dp,
        angle = 45f,
        segmentColor = SegmentColor(
            color = Color.White
        ),
        progressColor = SegmentColor(
            color = Color.Red,
            alpha = 0.3f,
        )
    )
}