package com.stephenvinouze.segmentedprogressbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.stephenvinouze.segmentedprogressbar.models.SegmentColor
import com.stephenvinouze.segmentedprogressbar.models.SegmentCoordinates
import com.stephenvinouze.segmentedprogressbar.ui.theme.Green200
import com.stephenvinouze.segmentedprogressbar.ui.theme.SegmentedProgressBarTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sample()
        }
    }
}

@Composable
fun Sample() {
    SegmentedProgressBarTheme {
        val kamehamehaSize = 38.dp
        val density = LocalDensity.current
        val progressAnimationDuration = 2000
        val breathEffectAnimationDuration = 1800

        // Segment progress bar properties
        var segmentCount by remember { mutableStateOf(3f) }
        var segmentSpacing by remember { mutableStateOf(10.dp) }
        var segmentAngle by remember { mutableStateOf(0f) }
        var segmentColor by remember { mutableStateOf(Color.Gray) }
        var segmentAlpha by remember { mutableStateOf(1f) }
        var progressColor by remember { mutableStateOf(Green200) }
        var progressAlpha by remember { mutableStateOf(1f) }
        var progress by remember { mutableStateOf(0f) }
        var drawBehindProgress by remember { mutableStateOf(true) }

        // Animation properties
        var enableBreathEffectAnimation by remember { mutableStateOf(false) }
        var enableKamehamehaAnimation by remember { mutableStateOf(false) }
        var offsetKamehameha by remember { mutableStateOf(0.dp) }
        var isProgressing by remember { mutableStateOf(false) }

        // Toggles to disable some properties for custom animations for rendering reasons
        var enableProgressAlpha by remember { mutableStateOf(true) }
        var enableAngle by remember { mutableStateOf(true) }

        /**
         * Example of custom animation with alpha breathing effect
         * Infinite animation is started when option is enabled and when progress is reaching previous to last segment, otherwise removed from composition
         * Using keyframes for higher customization. Animation is in four stages
         * 1. First half, keep progressAlpha
         * 2. From half to 75% duration, duck to 0.3% of progressAlpha
         * 3. From 75% to completion, restore to progressAlpha
         */
        val animatedProgressAlpha = if (enableBreathEffectAnimation && !isProgressing && progress.compareTo(segmentCount - 1) == 0) {
            val infiniteTransition = rememberInfiniteTransition()
            infiniteTransition.animateFloat(
                initialValue = progressAlpha,
                targetValue = progressAlpha,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = breathEffectAnimationDuration
                        progressAlpha.at(breathEffectAnimationDuration / 2)
                        (progressAlpha * 0.3f).at(3 * breathEffectAnimationDuration / 4)
                    },
                    repeatMode = RepeatMode.Restart,
                ),
            ).value
        } else {
            progressAlpha
        }

        Surface(
            modifier = Modifier.fillMaxHeight(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
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
                            color = segmentColor,
                            alpha = segmentAlpha,
                        ),
                        progressColor = SegmentColor(
                            color = progressColor,
                            alpha = animatedProgressAlpha,
                        ),
                        drawSegmentsBehindProgress = drawBehindProgress,
                        progressAnimationSpec = tween(
                            durationMillis = progressAnimationDuration,
                            easing = LinearEasing,
                        ),
                        onProgressChanged = { _: Float, progressCoordinates: SegmentCoordinates ->
                            isProgressing = true
                            drawBehindProgress = true
                            offsetKamehameha = density.run { progressCoordinates.topRightX.toDp() } - kamehamehaSize
                        },
                        onProgressFinished = {
                            isProgressing = false
                            drawBehindProgress = !enableBreathEffectAnimation
                        }
                    )

                    if (enableKamehamehaAnimation && isProgressing) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_kamehameha))
                        LottieAnimation(
                            modifier = Modifier
                                .size(kamehamehaSize)
                                .absoluteOffset(x = offsetKamehameha),
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                        )
                    }
                }

                Stepper(
                    modifier = Modifier.padding(top = 20.dp),
                    label = "Number of segments: ${segmentCount.toInt()}",
                    onMinus = { if (segmentCount > 1) segmentCount-- },
                    onPlus = { segmentCount++ },
                )

                Stepper(
                    modifier = Modifier.padding(top = 20.dp),
                    label = "Progress: ${progress.toInt()}",
                    onMinus = { if (progress > 0) progress-- },
                    onPlus = { if (progress < segmentCount) progress++ },
                )

                RangePicker(
                    modifier = Modifier.padding(top = 20.dp),
                    title = "Spacing: ${segmentSpacing.value}",
                    value = segmentSpacing.value,
                    range = 0f..100f,
                    onValueChanged = { segmentSpacing = Dp(it) },
                )

                RangePicker(
                    modifier = Modifier.padding(top = 20.dp),
                    title = "Angle: ${segmentAngle.toInt()}°",
                    value = segmentAngle,
                    range = -60f..60f,
                    onValueChanged = { segmentAngle = it },
                    enabled = enableAngle,
                )

                RangePicker(
                    modifier = Modifier.padding(top = 20.dp),
                    title = "Segment alpha: $segmentAlpha",
                    value = segmentAlpha,
                    range = 0f..1f,
                    onValueChanged = { segmentAlpha = it },
                )

                RangePicker(
                    modifier = Modifier.padding(top = 20.dp),
                    title = "Progress alpha: $progressAlpha",
                    value = progressAlpha,
                    range = 0f..1f,
                    onValueChanged = { progressAlpha = it },
                    enabled = enableProgressAlpha,
                )

                LabelledSwitch(
                    modifier = Modifier.padding(top = 20.dp),
                    title = "Enabled last segment alpha animation",
                    checked = enableBreathEffectAnimation,
                    onCheckedChange = {
                        enableBreathEffectAnimation = it
                        enableProgressAlpha = !it
                        progressAlpha = if (it) 1f else progressAlpha
                    }
                )

                LabelledSwitch(
                    modifier = Modifier.padding(top = 20.dp),
                    title = "Enabled kamehameha animation",
                    checked = enableKamehamehaAnimation,
                    onCheckedChange = {
                        enableKamehamehaAnimation = it
                        enableAngle = !it
                        segmentAngle = if (it) 25f else segmentAngle
                    }
                )

                ColorPicker(
                    modifier = Modifier.padding(top = 20.dp),
                    title = "Pick segment color",
                    color = segmentColor,
                    onColorPicked = {
                        segmentColor = it
                    }
                )

                ColorPicker(
                    modifier = Modifier.padding(top = 20.dp),
                    title = "Pick progress color",
                    color = progressColor,
                    onColorPicked = {
                        progressColor = it
                    }
                )
            }
        }
    }
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
    val context = LocalContext.current

    OutlinedButton(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color,
        ),
        onClick = {
            ColorPickerDialog
                .Builder(context)
                .setTitle(title)
                .setDefaultColor(Color.Gray.toArgb())
                .setColorListener { color, _ ->
                    onColorPicked(Color(color))
                }
                .show()
        }) {
        Text(
            text = title,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Sample()
}