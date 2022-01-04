package com.stephenvinouze.segmentedprogressbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.stephenvinouze.segmentedprogressbar.models.SegmentColor
import com.stephenvinouze.segmentedprogressbar.models.SegmentedProgressBarViewState
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
        var progress by remember { mutableStateOf(0f) }

        Surface(
            modifier = Modifier.fillMaxHeight(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Center,
                ) {
                    SegmentedProgressBar(
                        viewState = SegmentedProgressBarViewState(
                            segmentCount = segmentCount.toInt(),
                            spacing = segmentSpacing,
                            angle = segmentAngle,
                            progress = progress,
                            segmentColor = SegmentColor(
                                color = Color.Gray,
                                alpha = 0.3f,
                            ),
                            progressColor = SegmentColor(
                                color = Color.Green,
                            ),
                        ),
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .height(30.dp),
                        onProgressChanged = {
                            println("on progress changed $it")
                        },
                        onProgressFinished = {
                            println("on progress finished $it")
                        }
                    )
                }

                Stepper(
                    label = "Number of segments: ${segmentCount.toInt()}",
                    onMinus = { segmentCount-- },
                    onPlus = { segmentCount++ },
                )

                Stepper(
                    label = "Progress: ${progress.toInt()}",
                    onMinus = { progress-- },
                    onPlus = { progress++ },
                )

                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = "Spacing between segments: ${segmentSpacing.value}"
                )

                Slider(
                    value = segmentSpacing.value,
                    valueRange = 0f..100f,
                    onValueChange = { segmentSpacing = Dp(it) },
                )

                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = "Angle of segments: ${segmentAngle.toInt()}°"
                )

                Slider(
                    value = segmentAngle,
                    valueRange = -80f..80f,
                    onValueChange = { segmentAngle = it },
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
) {
    Row(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth(),
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Sample()
}