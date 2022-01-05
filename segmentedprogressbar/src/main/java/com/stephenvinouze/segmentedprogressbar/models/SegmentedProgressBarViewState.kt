package com.stephenvinouze.segmentedprogressbar.models

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class SegmentedProgressBarViewState(
    @IntRange(from = 1) val segmentCount: Int,
    @FloatRange(from = 0.0) val progress: Float = 0f,
    @FloatRange(from = 0.0) val spacing: Dp = 0.dp,
    @FloatRange(from = -80.0, to = 80.0) val angle: Float = 0f, // Reaching 90° is pointless since the bevel will be horizontal
    val segmentColor: SegmentColor = SegmentColor(),
    val progressColor: SegmentColor = SegmentColor(),
)
