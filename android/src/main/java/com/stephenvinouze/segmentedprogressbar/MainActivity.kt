package com.stephenvinouze.segmentedprogressbar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var offsetKamehameha by remember { mutableStateOf(0f) }
                var showKamehameha by remember { mutableStateOf(false) }

                App(
                    kamehamehaAnimationContainer = {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = showKamehameha,
                        ) {
                            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_kamehameha))
                            LottieAnimation(
                                modifier = Modifier
                                    .size(kamehamehaSize)
                                    .graphicsLayer(translationX = offsetKamehameha),
                                composition = composition,
                                iterations = LottieConstants.IterateForever,
                            )
                        }
                    },
                    onKamehamehaToggle = { showKamehameha = it },
                    onKamehamehaOffsetChange = { offsetKamehameha = it }
                )
            }
        }
    }
}

@Preview
@Composable
fun Sample() {
    App()
}