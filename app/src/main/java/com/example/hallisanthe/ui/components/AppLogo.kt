package com.example.hallisanthe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hallisanthe.ui.theme.DeepGreen
import com.example.hallisanthe.ui.theme.EarthWhite
import com.example.hallisanthe.ui.theme.LightGreen

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    size: Dp = 72.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(10.dp, CircleShape)
            .background(
                brush = Brush.linearGradient(listOf(DeepGreen, LightGreen)),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Eco,
            contentDescription = null,
            tint = EarthWhite,
            modifier = Modifier.size(size * 0.54f)
        )
    }
}
