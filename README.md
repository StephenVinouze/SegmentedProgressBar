<h1 align="center">:traffic_light: SegmentedProgressBar :traffic_light:</h1>
<br/>
<p align="center">
Beautiful progress bar split into several segments. Highly customizable. Fully written with Jetpack Compose.
</p>

<p align="center">
  <a href="https://jitpack.io/#com.stephenvinouze/SegmentedProgressBar"><img alt="License" src="https://jitpack.io/v/com.stephenvinouze/SegmentedProgressBar.svg"/></a>
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://github.com/StephenVinouze/SegmentedProgressBar/actions"><img alt="Build Status" src="https://github.com/StephenVinouze/SegmentedProgressBar/workflows/Android%20CI/badge.svg"/></a> 
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
  <br />
  <a href="https://proandroiddev.com/delight-your-users-with-a-segmented-progress-bar-75cb18489585"><img alt="Medium" src="https://img.shields.io/static/v1?label=Story&message=Medium&logo=medium&color=000000"/></a>
  <a href="https://devlibrary.withgoogle.com/products/android/repos/StephenVinouze-SegmentedProgressBar"><img alt="Google" src="https://img.shields.io/badge/Google-Developer%20Library-orange?style=flat&logo=android"/></a>
  <a href="https://android-arsenal.com/details/1/8352"><img alt="API" src="https://img.shields.io/badge/Android%20Arsenal-SegmentedProgressBar-green.svg?style=flat"/></a>
</p>

<br/>
<p align="center">
  <img src="https://user-images.githubusercontent.com/14751184/149517865-1cde217c-d6e1-4d53-a415-f029a8492728.gif" />
</p>
<br/>
<p align="center">
  <img src="https://user-images.githubusercontent.com/14751184/149517870-f3eb519c-ddff-4924-9a9b-511c87b23f4f.gif" />
</p>


## Why this library?
Have you ever needed to display a progression showing several completion steps? The Android SDK offers a ProgressBar component that comes with a single segment. When you want to give a compelling interface showing the actual progression of your users, it looks friendlier ??? and prettier! ??? to show a segmented progression rather than a linear progression partially filled.

This component allows you to split your progression into distinct segments. It goes even further by letting you customize its display with attributes such as spacing between segments as well as a bevel to embellish your design.

The sample speaks for itself

<p align="center">
  <img src="https://user-images.githubusercontent.com/14751184/149315010-4e406182-41b0-4fb8-8ff3-46489a4c4584.gif" />
</p>


## Including in your project
Because the library is written with Jetpack Compose, it requires your project to use Kotlin to inflate this component. You can safely insert it in a non-Compose project with a `ComposeView` as long as it uses Kotlin.

Add Jitpack (where the library is hosted) in your root `build.gradle` file:

```groovy
allprojects {
  repositories {
    maven { url "https://jitpack.io" }
  }
}
```

Then in your `build.gradle` module file where you want to use this library:

```groovy
dependencies {
  implementation "com.stephenvinouze:SegmentedProgressBar:{latest_version}"
}

```

## How to use
Only the segmentCount is required to build the progress bar.

### From Compose:
```kotlin
setContent {
  YourTheme {
    SegmentedProgressBar(
      segmentCount = 3,
    )
  }
}
```

Where `YourTheme` is the `Theme` you're using in your application.

### From XML:
```xml
<androidx.compose.ui.platform.ComposeView
  android:id="@+id/segmented_progress_bar"
  android:layout_width="wrap_content"
  android:layout_height="wrap_content" />
```

Then in your code:

```kotlin
findViewById<ComposeView>(R.id.segmented_progress_bar).setContent {
  YourTheme {
    Surface {
      SegmentedProgressBar(
        segmentCount = 3,
      )
    }
  }
}
```

If you'd like to discover what this component offers, here is an exhaustive description of what it's capable of.

```kotlin
SegmentedProgressBar(
  segmentCount = 3,
  modifier = Modifier,
  spacing = 10.dp,
  angle = 30f, // Can also be negative to invert the bevel side
  progress = 1,
  segmentColor = SegmentColor(
    color = Color.Gray,
    alpha = 0.3f,
  ),
  progressColor = SegmentColor(
    color = Color.Green,
    alpha = 1f,
  ),
  drawSegmentsBehindProgress = false, // See Javadoc for more explanation on this parameter
  progressAnimationSpec = tween( // You can give any animation spec you'd like
    durationMillis = 1000,
    easing = LinearEasing,
  ),
  onProgressChanged = { progress: Float, progressCoordinates: SegmentCoordinates ->
    // Get notified at each recomposition cycle when a progression occurs.
    // You can use the current progression or the coordinates the progress segment currently has.
  },
  onProgressFinished = {
    // Get notified when the progression animation ends.
  }
)
```

I encourage you to play around with the sample to get a better look and feel. It show cases advanced usage with custom animation based on the progression callbacks that aren't part of the library itself.

# License
```xml
Copyright 2022 Stephen Vinouze

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
