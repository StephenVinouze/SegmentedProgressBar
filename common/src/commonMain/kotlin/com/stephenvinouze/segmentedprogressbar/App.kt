package com.stephenvinouze.segmentedprogressbar

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.stephenvinouze.segmentedprogressbar.models.SegmentColor
import com.stephenvinouze.segmentedprogressbar.models.SegmentCoordinates

val kamehamehaSize = 38.dp
private val Green200 = Color(0xFF5FC626)

private enum class ProgressState {
    Idle, Progressing
}

@Composable
fun App(
    kamehamehaAnimationContainer: @Composable () -> Unit = {},
    onKamehamehaToggle: (Boolean) -> Unit = {},
    onKamehamehaOffsetChange: (Float) -> Unit = {},
) {
    val density = LocalDensity.current

    // Animation properties
    val progressAnimationDuration = 1000
    val breathEffectAnimationDuration = 1800
    var enableBreathEffectAnimation by remember { mutableStateOf(false) }
    var enableKamehamehaAnimation by remember { mutableStateOf(false) }

    // Toggles to disable some properties for custom animations for rendering reasons
    val enableProgressAlpha by remember {
        derivedStateOf { !enableBreathEffectAnimation }
    }
    val enableAngle by remember {
        derivedStateOf { !enableKamehamehaAnimation }
    }

    // Segment progress bar properties
    var segmentCount by remember { mutableStateOf(3f) }
    var segmentSpacing by remember { mutableStateOf(10.dp) }
    var segmentAngle by remember { mutableStateOf(0f) }
    var segmentColor by remember { mutableStateOf(SegmentColor(Color.Gray, 1f)) }
    var progressColor by remember { mutableStateOf(SegmentColor(Green200, 1f)) }
    var progress by remember { mutableStateOf(0f) }
    var progressState by remember { mutableStateOf(ProgressState.Idle) }
    val drawBehindProgress by remember {
        derivedStateOf { !enableBreathEffectAnimation || progressState == ProgressState.Progressing }
    }

    // Breath effect animation properties
    /**
     * Example of custom animation with alpha breathing effect
     * Infinite animation is started when option is enabled and when progress is reaching previous to last segment, otherwise removed from composition
     * Using keyframes for higher customization. Animation is in four stages
     * 1. First half, keep progressAlpha
     * 2. From half to 75% duration, duck to 0.3% of progressAlpha
     * 3. From 75% to completion, restore to progressAlpha
     */
    val animatedProgressAlpha = if (enableBreathEffectAnimation && progressState == ProgressState.Idle && progress.compareTo(segmentCount - 1) == 0) {
        val infiniteTransition = rememberInfiniteTransition()
        infiniteTransition.animateFloat(
            initialValue = progressColor.alpha,
            targetValue = progressColor.alpha,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = breathEffectAnimationDuration
                    progressColor.alpha.at(breathEffectAnimationDuration / 2)
                    (progressColor.alpha * 0.3f).at(3 * breathEffectAnimationDuration / 4)
                },
                repeatMode = RepeatMode.Restart,
            ),
        ).value
    } else {
        progressColor.alpha
    }

    Surface(
        modifier = Modifier.fillMaxHeight(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clipToBounds(),
                contentAlignment = CenterStart,
            ) {

                SegmentedProgressBar(
                    modifier = Modifier.height(16.dp),
                    segmentCount = segmentCount.toInt(),
                    spacing = segmentSpacing,
                    angle = segmentAngle,
                    progress = progress,
                    segmentColor = SegmentColor(
                        color = segmentColor.color,
                        alpha = segmentColor.alpha,
                    ),
                    progressColor = SegmentColor(
                        color = progressColor.color,
                        alpha = animatedProgressAlpha,
                    ),
                    drawSegmentsBehindProgress = drawBehindProgress,
                    progressAnimationSpec = tween(
                        durationMillis = progressAnimationDuration,
                        easing = LinearEasing,
                    ),
                    onProgressChanged = { _: Float, progressCoordinates: SegmentCoordinates ->
                        progressState = ProgressState.Progressing
                        onKamehamehaToggle(enableKamehamehaAnimation)
                        onKamehamehaOffsetChange(progressCoordinates.topRightX - density.run { kamehamehaSize.toPx() })
                    },
                    onProgressFinished = {
                        progressState = ProgressState.Idle
                        onKamehamehaToggle(false)
                    }
                )

                kamehamehaAnimationContainer()
            }

            Configurator(
                segmentCount = segmentCount,
                segmentSpacing = segmentSpacing,
                segmentAngle = segmentAngle,
                progress = progress,
                segmentColor = segmentColor,
                progressColor = progressColor,
                enableAngle = enableAngle,
                enableProgressAlpha = enableProgressAlpha,
                enableBreathEffectAnimation = enableBreathEffectAnimation,
                enableKamehamehaAnimation = enableKamehamehaAnimation,
                onSegmentCountChange = { segmentCount = it },
                onProgressChange = { progress = it },
                onSpacingChange = { segmentSpacing = it },
                onAngleChange = { segmentAngle = it },
                onSegmentColorChange = { segmentColor = it },
                onProgressColorChange = { progressColor = it },
                onBreathEffectAnimationToggle = { enableBreathEffectAnimation = it },
                onKamehamehaAnimationToggle = { enableKamehamehaAnimation = it },
                modifier = Modifier.padding(top = 20.dp),
            )
        }
    }
}

@Composable
fun Configurator(
    segmentCount: Float,
    segmentSpacing: Dp,
    segmentAngle: Float,
    progress: Float,
    segmentColor: SegmentColor,
    progressColor: SegmentColor,
    enableAngle: Boolean,
    enableProgressAlpha: Boolean,
    enableBreathEffectAnimation: Boolean,
    enableKamehamehaAnimation: Boolean,
    onSegmentCountChange: (Float) -> Unit,
    onProgressChange: (Float) -> Unit,
    onSpacingChange: (Dp) -> Unit,
    onAngleChange: (Float) -> Unit,
    onSegmentColorChange: (SegmentColor) -> Unit,
    onProgressColorChange: (SegmentColor) -> Unit,
    onBreathEffectAnimationToggle: (Boolean) -> Unit,
    onKamehamehaAnimationToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Stepper(
        modifier = modifier,
        label = "Number of segments: ${segmentCount.toInt()}",
        onMinus = {
            // Prevents having progress above segment count and less than one segment
            if (segmentCount > 1 && segmentCount > progress) onSegmentCountChange(segmentCount - 1)
        },
        onPlus = { onSegmentCountChange(segmentCount + 1) },
    )

    Stepper(
        modifier = Modifier.padding(top = 20.dp),
        label = "Progress: ${progress.toInt()}",
        onMinus = {
            // Prevents going below progress == 0
            if (progress > 0) onProgressChange(progress - 1)
        },
        onPlus = {
            // Prevents having progress above segment count
            if (progress < segmentCount) onProgressChange(progress + 1)
        },
    )

    RangePicker(
        modifier = Modifier.padding(top = 20.dp),
        title = "Spacing: ${segmentSpacing.value}",
        value = segmentSpacing.value,
        range = 0f..100f,
        onValueChanged = { onSpacingChange(Dp(it)) },
    )

    RangePicker(
        modifier = Modifier.padding(top = 20.dp),
        title = "Angle: ${segmentAngle.toInt()}°",
        value = segmentAngle,
        range = -60f..60f,
        onValueChanged = { onAngleChange(it) },
        enabled = enableAngle,
    )

    RangePicker(
        modifier = Modifier.padding(top = 20.dp),
        title = "Segment alpha: ${segmentColor.alpha}",
        value = segmentColor.alpha,
        range = 0f..1f,
        onValueChanged = { onSegmentColorChange(segmentColor.copy(alpha = it)) },
    )

    RangePicker(
        modifier = Modifier.padding(top = 20.dp),
        title = "Progress alpha: ${progressColor.alpha}",
        value = progressColor.alpha,
        range = 0f..1f,
        onValueChanged = { onProgressColorChange(progressColor.copy(alpha = it)) },
        enabled = enableProgressAlpha,
    )

    LabelledSwitch(
        modifier = Modifier.padding(top = 20.dp),
        title = "Enabled last segment alpha animation",
        checked = enableBreathEffectAnimation,
        onCheckedChange = {
            onBreathEffectAnimationToggle(it)
            if (it) onProgressColorChange(progressColor.copy(alpha = 1f))
        }
    )

    LabelledSwitch(
        modifier = Modifier.padding(top = 20.dp),
        title = "Enabled kamehameha animation",
        checked = enableKamehamehaAnimation,
        onCheckedChange = {
            onKamehamehaAnimationToggle(it)
            if (it) onAngleChange(25f)
        }
    )

    ColorPicker(
        modifier = Modifier.padding(top = 20.dp),
        title = "Pick segment color",
        color = segmentColor.color,
        onColorPicked = { onSegmentColorChange(segmentColor.copy(color = it)) }
    )

    ColorPicker(
        modifier = Modifier.padding(top = 20.dp),
        title = "Pick progress color",
        color = progressColor.color,
        onColorPicked = { onProgressColorChange(progressColor.copy(color = it)) }
    )
}

@Composable
fun Stepper(
    label: String,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
        )
        OutlinedButton(
            modifier = Modifier.padding(start = 5.dp),
            onClick = { onMinus() }
        ) {
            Text("-")
        }
        OutlinedButton(
            modifier = Modifier.padding(start = 5.dp),
            onClick = { onPlus() }
        ) {
            Text("+")
        }
    }
}

@Composable
fun RangePicker(
    title: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
        )

        Slider(
            value = value,
            valueRange = range,
            onValueChange = onValueChanged,
            modifier = Modifier.weight(1f),
            enabled = enabled,
        )
    }
}

@Composable
fun LabelledSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
fun ColorPicker(
    title: String,
    color: Color,
    onColorPicked: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    //val context = LocalContext.current

    OutlinedButton(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color,
        ),
        onClick = {
/*            ColorPickerDialog
                .Builder(context)
                .setTitle(title)
                .setDefaultColor(Color.Gray.toArgb())
                .setColorListener { color, _ ->
                    onColorPicked(Color(color))
                }
                .show()*/
        }) {
        Text(
            text = title,
        )
    }
}
