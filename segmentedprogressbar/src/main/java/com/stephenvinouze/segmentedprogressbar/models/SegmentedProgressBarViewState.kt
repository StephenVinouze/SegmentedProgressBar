package com.stephenvinouze.segmentedprogressbar.models

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class SegmentedProgressBarViewState(
    val segmentCount: Int,
    val progress: Float = 0f,
    val angle: Float = 0f,
    val spacing: Dp = 0.dp,
    val segmentColor: SegmentColor = SegmentColor(),
    val progressColor: SegmentColor = SegmentColor(),
)
