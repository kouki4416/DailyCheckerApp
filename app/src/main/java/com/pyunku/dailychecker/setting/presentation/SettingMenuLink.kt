package com.pyunku.dailychecker.setting.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingMenuLink(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
) {
    Surface {
        Row(
            modifier = modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onClick),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SettingsTileIcon(icon = icon)
                SettingsTileTexts(title = title, subtitle = subtitle)
            }
//            if (action != null) {
//                Divider(
//                    modifier = Modifier
//                        .padding(vertical = 4.dp)
//                        .height(56.dp)
//                        .width(1.dp),
//                )
//                SettingsTileAction {
//                    action.invoke()
//                }
//            }
        }
    }
}

@Composable
internal fun SettingsTileIcon(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
) {
    Box(
        modifier = modifier.size(64.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (icon != null) {
            icon()
        }
    }
}

@Composable
internal fun RowScope.SettingsTileTexts(
    title: @Composable () -> Unit,
    subtitle: @Composable (() -> Unit)?,
) {
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.Center,
    ) {
        SettingsTileTitle(title)
        if (subtitle != null) {
            Spacer(modifier = Modifier.size(2.dp))
            SettingsTileSubtitle(subtitle)
        }
    }
}

@Composable
internal fun SettingsTileAction(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.size(64.dp),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
internal fun SettingsTileTitle(title: @Composable () -> Unit) {
    ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
        title()
    }
}

@Composable
internal fun SettingsTileSubtitle(subtitle: @Composable () -> Unit) {
    ProvideTextStyle(value = MaterialTheme.typography.bodyMedium) {
        subtitle()
    }
}