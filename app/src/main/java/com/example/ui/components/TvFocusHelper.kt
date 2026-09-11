package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.YouTubeFocusBorder
import com.example.ui.theme.YouTubeRed

@Composable
fun Modifier.tvFocusable(
    shape: Shape = RoundedCornerShape(12.dp),
    focusedBorderColor: Color = YouTubeFocusBorder,
    focusedBorderWidth: Dp = 2.5.dp,
    scaleOnFocus: Float = 1.05f,
    onClick: () -> Unit = {},
    onFocusChange: (Boolean) -> Unit = {}
): Modifier {
    var isFocused by remember { mutableStateOf(false) }
    val animatedScale by animateFloatAsState(
        targetValue = if (isFocused) scaleOnFocus else 1f,
        animationSpec = tween(durationMillis = 180),
        label = "tvFocusScale"
    )

    return this
        .scale(animatedScale)
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
            onFocusChange(focusState.isFocused)
        }
        .border(
            width = if (isFocused) focusedBorderWidth else 0.dp,
            color = if (isFocused) focusedBorderColor else Color.Transparent,
            shape = shape
        )
        .onKeyEvent { keyEvent ->
            if (keyEvent.type == KeyEventType.KeyUp &&
                (keyEvent.key == Key.DirectionCenter || keyEvent.key == Key.Enter)
            ) {
                onClick()
                true
            } else {
                false
            }
        }
        .focusable()
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
}
