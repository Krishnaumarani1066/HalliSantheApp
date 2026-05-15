package com.example.hallisanthe.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.hallisanthe.ui.theme.DeepGreen
import com.example.hallisanthe.ui.theme.SoftMint
import kotlin.math.roundToInt

@Composable
fun TickerTape(
    text: String,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "ticker")
    val shift by transition.animateFloat(
        initialValue = 0f,
        targetValue = -42f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "tickerShift"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SoftMint)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Icons.Rounded.TrendingUp, contentDescription = null, tint = DeepGreen)
        Text(
            text = text,
            modifier = Modifier.offset { IntOffset(shift.roundToInt(), 0) },
            style = MaterialTheme.typography.bodyMedium,
            color = DeepGreen
        )
    }
}
