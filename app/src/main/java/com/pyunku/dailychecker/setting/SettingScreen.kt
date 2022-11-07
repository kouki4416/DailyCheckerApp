package com.pyunku.dailychecker.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alorma.compose.settings.ui.*

@Composable
fun SettingRoute(
    modifier: Modifier = Modifier,
) {
     Column() {
        SettingsMenuLink(
            icon = { Icon(imageVector = Icons.Default.Wifi, contentDescription = "Wifi") },
            title = { Text(text = "Link") },
            subtitle = { Text(text = "This is a longer text") },
            onClick = {},
        )
        Divider()
        SettingsSwitch(
            icon = { Icon(imageVector = Icons.Default.Wifi, contentDescription = "Wifi") },
            title = { Text(text = "Switch") },
            subtitle = { Text(text = "This is a longer text") },
            onCheckedChange = { },
        )
        Divider()
        SettingsCheckbox(
            icon = { Icon(imageVector = Icons.Default.Wifi, contentDescription = "Wifi") },
            title = { Text(text = "Checkbox") },
            subtitle = { Text(text = "This is a longer text") },
            onCheckedChange = { },
        )
        Divider()
        SettingsSlider(
            icon = {
                Icon(
                    imageVector = Icons.Default.BrightnessMedium,
                    contentDescription = "Brightness Medium"
                )
            },
            title = { Text(text = "Slider") },
        )
        Divider()
        SettingsList(
            title = { Text(text = "List") },
            subtitle = { Text(text = "Select a fruit") },
            items = listOf("Banana", "Kiwi", "Pineapple"),
            action = {
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                    )
                }
            },
        )
    }
//    val context = LocalContext.current
//    Button(onClick = { context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))}) {
//
//    }
}