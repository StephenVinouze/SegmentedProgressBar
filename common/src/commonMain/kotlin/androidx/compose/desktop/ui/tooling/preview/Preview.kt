package androidx.compose.desktop.ui.tooling.preview

@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
expect annotation class Preview(
    // TODO(mount): Make this Dp when they are inline classes
    val widthDp: Int = -1,
    // TODO(mount): Make this Dp when they are inline classes
    val heightDp: Int = -1,
)
