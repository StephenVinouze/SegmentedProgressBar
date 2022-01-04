/*
 * Copyright (c) 2019. Betclic. All rights reserved.
 */

package com.stephenvinouze.segmentedprogressbar

import com.stephenvinouze.segmentedprogressbar.models.SegmentCoordinates
import org.junit.Assert
import org.junit.Test

class SegmentedCoordinatesComputerTest {

    companion object {
        private const val DELTA = 0.1f
    }

    private val helper = SegmentCoordinatesComputer()

    @Test
    fun `segment coordinates for 1 segment`() {
        // GIVEN
        val segmentCount = 1
        val width = 100f
        val height = 20f
        val spacing = 0f
        val angle = 0f

        // WHEN
        val firstSegmentCoordinates = helper.segmentCoordinates(0, segmentCount, width, height, spacing, angle)

        // THEN
        firstSegmentCoordinates.shouldBeNear(0f, 0f, width, width)
    }

    @Test
    fun `segment coordinates for 2 segments without spacing`() {
        // GIVEN
        val segmentCount = 2
        val width = 100f
        val height = 20f
        val spacing = 0f
        val angle = 0f

        // WHEN
        val firstSegmentCoordinates = helper.segmentCoordinates(0, segmentCount, width, height, spacing, angle)
        val secondSegmentCoordinates = helper.segmentCoordinates(1, segmentCount, width, height, spacing, angle)

        // THEN
        firstSegmentCoordinates.shouldBeNear(0f, 0f, 50f, 50f)
        secondSegmentCoordinates.shouldBeNear(50f, 50f, width, width)
    }

    @Test
    fun `segment coordinates for 2 segments with spacing`() {
        // GIVEN
        val segmentCount = 2
        val width = 100f
        val height = 20f
        val spacing = 10f
        val angle = 0f

        // WHEN
        val firstSegmentCoordinates = helper.segmentCoordinates(0, segmentCount, width, height, spacing, angle)
        val secondSegmentCoordinates = helper.segmentCoordinates(1, segmentCount, width, height, spacing, angle)

        // THEN
        firstSegmentCoordinates.shouldBeNear(0f, 0f, 45f, 45f)
        secondSegmentCoordinates.shouldBeNear(55f, 55f, width, width)
    }

    @Test
    fun `segment coordinates for 3 segments without spacing`() {
        // GIVEN
        val segmentCount = 3
        val width = 100f
        val height = 20f
        val spacing = 0f
        val angle = 0f

        // WHEN
        val firstSegmentCoordinates = helper.segmentCoordinates(0, segmentCount, width, height, spacing, angle)
        val secondSegmentCoordinates = helper.segmentCoordinates(1, segmentCount, width, height, spacing, angle)
        val thirdSegmentCoordinates = helper.segmentCoordinates(2, segmentCount, width, height, spacing, angle)

        // THEN
        firstSegmentCoordinates.shouldBeNear(0f, 0f, 33.3f, 33.3f)
        secondSegmentCoordinates.shouldBeNear(33.3f, 33.3f, 66.6f, 66.6f)
        thirdSegmentCoordinates.shouldBeNear(66.6f, 66.6f, width, width)
    }

    @Test
    fun `segment coordinates for 3 segments with spacing`() {
        // GIVEN
        val segmentCount = 3
        val width = 100f
        val height = 20f
        val spacing = 10f
        val angle = 0f

        // WHEN
        val firstSegmentCoordinates = helper.segmentCoordinates(0, segmentCount, width, height, spacing, angle)
        val secondSegmentCoordinates = helper.segmentCoordinates(1, segmentCount, width, height, spacing, angle)
        val thirdSegmentCoordinates = helper.segmentCoordinates(2, segmentCount, width, height, spacing, angle)

        // THEN
        firstSegmentCoordinates.shouldBeNear(0f, 0f, 26.6f, 26.6f)
        secondSegmentCoordinates.shouldBeNear(36.6f, 36.6f, 63.3f, 63.3f)
        thirdSegmentCoordinates.shouldBeNear(73.3f, 73.3f, width, width)
    }

    @Test
    fun `segment coordinates for 3 segments with spacing and positive angle`() {
        // GIVEN
        val segmentCount = 3
        val width = 100f
        val height = 20f
        val spacing = 10f
        val angle = 30f

        // WHEN
        val firstSegmentCoordinates = helper.segmentCoordinates(0, segmentCount, width, height, spacing, angle)
        val secondSegmentCoordinates = helper.segmentCoordinates(1, segmentCount, width, height, spacing, angle)
        val thirdSegmentCoordinates = helper.segmentCoordinates(2, segmentCount, width, height, spacing, angle)

        // THEN
        firstSegmentCoordinates.shouldBeNear(0f, 0f, 30.5f, 19.0f)
        secondSegmentCoordinates.shouldBeNear(40.5f, 29.0f, 71.0f, 59.5f)
        thirdSegmentCoordinates.shouldBeNear(81.0f, 69.5f, width, width)
    }

    @Test
    fun `segment coordinates for 3 segments with spacing and negative angle`() {
        // GIVEN
        val segmentCount = 3
        val width = 100f
        val height = 20f
        val spacing = 10f
        val angle = -30f

        // WHEN
        val firstSegmentCoordinates = helper.segmentCoordinates(0, segmentCount, width, height, spacing, angle)
        val secondSegmentCoordinates = helper.segmentCoordinates(1, segmentCount, width, height, spacing, angle)
        val thirdSegmentCoordinates = helper.segmentCoordinates(2, segmentCount, width, height, spacing, angle)

        // THEN
        firstSegmentCoordinates.shouldBeNear(0f, 0f, 22.8f, 34.4f)
        secondSegmentCoordinates.shouldBeNear(32.8f, 44.4f, 55.6f, 67.2f)
        thirdSegmentCoordinates.shouldBeNear(65.6f, 77.2f, width, width)
    }

    @Test
    fun `progress coordinates for 3 segments with spacing and positive angle`() {
        // GIVEN
        val segmentCount = 3
        val width = 100f
        val height = 20f
        val spacing = 10f
        val angle = 30f

        // WHEN
        val noProgressionSegmentCoordinates = helper.progressCoordinates(0f, segmentCount, width, height, spacing, angle)
        val oneProgressionSegmentCoordinates = helper.progressCoordinates(1f, segmentCount, width, height, spacing, angle)
        val twoProgressionSegmentCoordinates = helper.progressCoordinates(2f, segmentCount, width, height, spacing, angle)
        val fullProgressionSegmentCoordinates = helper.progressCoordinates(segmentCount.toFloat(), segmentCount, width, height, spacing, angle)

        // THEN
        noProgressionSegmentCoordinates.shouldBeNear(0f, 0f, 0f, 0f)
        oneProgressionSegmentCoordinates.shouldBeNear(0f, 0f, 30.5f, 19.0f)
        twoProgressionSegmentCoordinates.shouldBeNear(0f, 0f, 71.0f, 59.5f)
        fullProgressionSegmentCoordinates.shouldBeNear(0f, 0f, width, width)
    }

    @Test
    fun `progress coordinates for 3 segments with spacing and negative angle`() {
        // GIVEN
        val segmentCount = 3
        val width = 100f
        val height = 20f
        val spacing = 10f
        val angle = -30f

        // WHEN
        val noProgressionSegmentCoordinates = helper.progressCoordinates(0f, segmentCount, width, height, spacing, angle)
        val oneProgressionSegmentCoordinates = helper.progressCoordinates(1f, segmentCount, width, height, spacing, angle)
        val twoProgressionSegmentCoordinates = helper.progressCoordinates(2f, segmentCount, width, height, spacing, angle)
        val fullProgressionSegmentCoordinates = helper.progressCoordinates(segmentCount.toFloat(), segmentCount, width, height, spacing, angle)

        // THEN
        noProgressionSegmentCoordinates.shouldBeNear(0f, 0f, 0f, 0f)
        oneProgressionSegmentCoordinates.shouldBeNear(0f, 0f, 22.8f, 34.4f)
        twoProgressionSegmentCoordinates.shouldBeNear(0f, 0f, 55.6f, 67.2f)
        fullProgressionSegmentCoordinates.shouldBeNear(0f, 0f, width, width)
    }

    private fun SegmentCoordinates.shouldBeNear(topLeft: Float, bottomLeft: Float, topRight: Float, bottomRight: Float) {
        Assert.assertEquals(topLeft, topLeftX, DELTA)
        Assert.assertEquals(bottomLeft, bottomLeftX, DELTA)
        Assert.assertEquals(topRight, topRightX, DELTA)
        Assert.assertEquals(bottomRight, bottomRightX, DELTA)
    }
}