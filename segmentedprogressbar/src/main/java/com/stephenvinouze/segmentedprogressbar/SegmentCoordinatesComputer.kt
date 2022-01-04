package com.stephenvinouze.segmentedprogressbar

import com.stephenvinouze.segmentedprogressbar.models.SegmentCoordinates
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.tan

class SegmentCoordinatesComputer {

    /**
     * Compute segment coordinates depending on several parameters
     *
     * Will result in X coordinates of topLeft, topRight, bottomLeft and bottomRight per segment
     *
     *
     *             segmentTangent        spacing
     *                  <->                <->
     *    -----------------   -------------   ------------   ^
     *   |                /               /               |  |  height
     *   |               /               /                |  |
     *    --------------   -------------   ---------------   ^
     *    <------------>
     *     segmentWidth
     *
     *    <----------------------------------------------->
     *                          width
     *
     *
     * @param position The segment position in the progress bar
     * @param segmentCount The segment count in the progress bar
     * @param width The progress bar's width
     * @param height The progress bar's height
     * @param spacing The spacing between each segment
     * @param angle The angle between each segment
     */
    fun segmentCoordinates(position: Int, segmentCount: Int, width: Float, height: Float, spacing: Float, angle: Float): SegmentCoordinates {
        val segmentTangent = segmentTangent(height, angle)
        val segmentWidth = (width - (spacing + segmentTangent) * (segmentCount - 1)) / segmentCount
        val isLast = position == segmentCount - 1

        val topLeft = (segmentWidth + segmentTangent + spacing) * position
        val bottomLeft = (segmentWidth + spacing) * position + segmentTangent * max(0, position - 1)
        val topRight = (segmentWidth + segmentTangent) * (position + 1) + spacing * position - if (isLast) segmentTangent else 0f
        val bottomRight = segmentWidth * (position + 1) + (segmentTangent + spacing) * position

        return SegmentCoordinates(topLeft, topRight, bottomLeft, bottomRight)
    }

    /**
     * Compute progress coordinates depending on several parameters
     *
     * Will result in X coordinates of topLeft, topRight, bottomLeft and bottomRight for progress segment
     *
     *
     *                             segmentTangent
     *                                  <->
     *                                        segmentWidth
     *                                        <----------->
     *    ---------------------------------   ------------   ^
     *   |                                /               |  |  height
     *   |                               /                |  |
     *    ------------------------------   ---------------   ^
     *                                  <->
     *                                spacing
     *
     *    <----------------------------------------------->
     *                          width
     *
     *
     * @param progress The current progress in the progress bar (interpolated by animatedFloatAsState)
     * @param segmentCount The segment count in the progress bar
     * @param width The progress bar's width
     * @param height The progress bar's height
     * @param spacing The spacing between each segment
     * @param angle The angle between each segment
     */
    fun progressCoordinates(progress: Float, segmentCount: Int, width: Float, height: Float, spacing: Float, angle: Float): SegmentCoordinates {
        val segmentTangent = segmentTangent(height, angle)
        val segmentWidth = (width - (spacing + segmentTangent) * (segmentCount - 1)) / segmentCount
        val spacingCount = maxOf(0f, progress - 1)
        val isLastSegment = ceil(progress).toInt() == segmentCount
        val lastSegmentOffset = if (isLastSegment) segmentTangent * (1 - (segmentCount - progress)) else 0f

        val topRight = (segmentWidth + segmentTangent) * progress + spacing * spacingCount - lastSegmentOffset
        val bottomRight = segmentWidth * progress + (segmentTangent + spacing) * spacingCount

        return SegmentCoordinates(0f, topRight, 0f, bottomRight)
    }

    private fun segmentTangent(height: Float, angle: Float): Float =
        height * tan(Math.toRadians(angle.toDouble())).toFloat()
}