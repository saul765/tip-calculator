package com.example.tipcalulator.ui.base

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


private val iconButtonSizeModifier = Modifier.padding(8.dp)

@Composable
fun RoundIconButton(
    modifier: Modifier,
    imageVector: ImageVector = Icons.Rounded.Android,
    onClick: () -> Unit,
    @StringRes contentDescription: Int,
    tint: Color = Color.Black.copy(alpha = 0.8f),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    elevation: Dp = 4.dp,
    buttonSize: Dp = 40.dp
) {
    Card(
        modifier = modifier
            .size(buttonSize)
            .clickable { onClick.invoke() }
            .then(iconButtonSizeModifier),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(elevation),
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(id = contentDescription),
            tint = tint,
            modifier = Modifier.size(buttonSize)
        )

    }
}