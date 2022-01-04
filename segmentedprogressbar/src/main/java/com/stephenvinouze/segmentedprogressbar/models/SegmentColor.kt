package com.stephenvinouze.segmentedprogressbar.models

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color

data class SegmentColor(
    val color: Color = Color.Green,
    @FloatRange(from = 0.0, to = 1.0) val alpha: Float = 1.0f,
)
