package com.stephenvinouze.segmentedprogressbar

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.*
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

/**
 * Progress bar split into several segments.
 * Comes with several attributes to customize appearance. Some are constrained within ranges to prevent erratic display behaviors (such as angle, see below)
 *
 * Note that [drawSegmentsBehindProgress] is to be used with caution. Especially when applying an alpha on the progress bar. If you don't, we recommend disabling it for performance reasons.
 * Enabling it is useful when you'd like to perform a fade animation on the progress segment. To improve the rendering, we don't want to see the segments behind the progress (that will become visible during the fade animation otherwise).
 * If you need to apply an opacity on the progress bar, mind that:
 * - enabling it will keep drawing the segments behind the progress. Since you apply an opacity on the progress, you'll see the spacing between segments behind the progress segment.
 * - disabling it will keep drawing the segment being progressed during the animation (that's expected). Once the animation completes, the segment will disappear. Applying an opacity on the progress segment will make this behavior visible to the user, giving a bad user experience since all previous segments are already hidden.
 *
 * To bypass this issue, it's best to change its value outside the animation recomposition. You can make use of the [onProgressFinished] callback for instance. See breath effect animation in the Sample.
 *
 * Bevels are expressed in degrees for convenience and internally converted in Radians. Angle can be negative, but is contained between -60° and 60° otherwise the bevel breaks segments by nearing an horizontal plane.
 *
 * @param segmentCount The number of segments displayed on the progress bar
 * @param modifier [Modifier] to apply to this layout node
 * @param progress The progress value to display the progress segment
 * @param spacing The spacing between segments
 * @param angle The bevel for each segment
 * @param segmentColor [SegmentColor] for each segment
 * @param progressColor [SegmentColor] for the progress segment
 * @param drawSegmentsBehindProgress The flag to enable drawing segments behind progress knowing it will cover the segments
 * @param progressAnimationSpec [AnimationSpec] for the progress animation
 * @param onProgressChanged The callback triggered when a progression animation occurs. Gives the current progression and the current progress coordinates while animating
 * @param onProgressFinished The callback triggered when a progression animation finishes.
 */
@Composable
fun SegmentedProgressBar(
    @IntRange(from = 1) segmentCount: Int,
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0) progress: Float = 0f,
    @FloatRange(from = 0.0) spacing: Dp = 0.dp,
    @FloatRange(from = -60.0, to = 60.0) angle: Float = 0f,
    segmentColor: SegmentColor = SegmentColor(),
    progressColor: SegmentColor = SegmentColor(),
    drawSegmentsBehindProgress: Boolean = false,
    progressAnimationSpec: AnimationSpec<Float> = tween(),
    onProgressChanged: ((progress: Float, progressCoordinates: SegmentCoordinates) -> Unit)? = null,
    onProgressFinished: ((progress: Float) -> Unit)? = null,
) {
    val computer = remember { SegmentCoordinatesComputer() }
    var progressCoordinates by remember { mutableStateOf(SegmentCoordinates(0f, 0f, 0f, 0f)) }
    val spacingPx = LocalDensity.current.run { spacing.toPx() }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = progressAnimationSpec,
        finishedListener = onProgressFinished,
    )

    if (animatedProgress.compareTo(progress) != 0) {
        onProgressChanged?.invoke(animatedProgress, progressCoordinates)
    }

    val progressRange = 0f..segmentCount.toFloat()

    Canvas(
        modifier = modifier
            .progressSemantics(
                value = progress,
                valueRange = progressRange,
            )
            .fillMaxWidth()
            .clipToBounds(),
        onDraw = {
            progressCoordinates = computer.progressCoordinates(
                progress = animatedProgress.coerceIn(progressRange),
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
                if (drawSegmentsBehindProgress || segmentCoordinates.topRightX.compareTo(progressCoordinates.topRightX) > 0) {
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