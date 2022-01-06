package com.stephenvinouze.segmentedprogressbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.stephenvinouze.segmentedprogressbar.models.SegmentColor
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
        var segmentCount by remember { mutableStateOf(3f) }
        var segmentSpacing by remember { mutableStateOf(10.dp) }
        var segmentAngle by remember { mutableStateOf(0f) }
        var segmentColor by remember { mutableStateOf(Color.Gray) }
        var segmentAlpha by remember { mutableStateOf(1f) }
        var progressColor by remember { mutableStateOf(Color.Green) }
        var progressAlpha by remember { mutableStateOf(1f) }
        var progress by remember { mutableStateOf(0f) }

        Surface(
            modifier = Modifier.fillMaxHeight(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Center,
                ) {
                    SegmentedProgressBar(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .height(30.dp),
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
                            alpha = progressAlpha,
                        ),
                        onProgressChanged = {
                            println("on progress changed $it")
                        },
                        onProgressFinished = {
                            println("on progress finished $it")
                        }
                    )
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