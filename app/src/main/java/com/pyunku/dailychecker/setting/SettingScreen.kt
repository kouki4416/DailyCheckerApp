package com.pyunku.dailychecker.setting

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.alorma.compose.settings.storage.base.SettingValueState
import com.alorma.compose.settings.storage.base.rememberIntSettingState
import com.alorma.compose.settings.ui.SettingsList
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.pyunku.dailychecker.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingRoute(
    modifier: Modifier = Modifier,

) {
    val context = LocalContext.current
    Column() {
        SettingsMenuLink(
            title = { Text(text = stringResource(R.string.License)) },
            onClick = {
                context.startActivity(Intent(context,
                    OssLicensesMenuActivity::class.java))
            },
        )
        Divider()
//        SettingsSwitch(
//            icon = { Icon(imageVector = Icons.Default.Wifi, contentDescription = "Wifi") },
//            title = { Text(text = "Switch") },
//            subtitle = { Text(text = "This is a longer text") },
//            onCheckedChange = { },
//        )
//        Divider()
//        SettingsCheckbox(
//            icon = { Icon(imageVector = Icons.Default.Wifi, contentDescription = "Wifi") },
//            title = { Text(text = "Checkbox") },
//            subtitle = { Text(text = "This is a longer text") },
//            onCheckedChange = { },
//        )
//        Divider()
//        SettingsSlider(
//            icon = {
//                Icon(
//                    imageVector = Icons.Default.BrightnessMedium,
//                    contentDescription = "Brightness Medium"
//                )
//            },
//            title = { Text(text = "Slider") },
//        )
//        Divider()
        SettingsList(
            title = { Text(text = "List") },
            subtitle = { Text(text = "Select a fruit") },
            items = listOf("Banana", "Kiwi", "Pineapple"),
            action = {
                IconButton(onClick = {

                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                    )
                }
            },
        )
    }
}

@Composable
fun SettingsList(
    modifier: Modifier = Modifier,
    state: SettingValueState<Int> = rememberIntSettingState(),
    title: @Composable () -> Unit,
    items: List<String>,
    icon: (@Composable () -> Unit)? = null,
    useSelectedValueAsSubtitle: Boolean = true,
    subtitle: (@Composable () -> Unit)? = null,
    closeDialogDelay: Long = 200,
    action: (@Composable () -> Unit)? = null,
) {

    if (state.value >= items.size) {
        throw IndexOutOfBoundsException("Current value for $title list setting cannot be grater than items size")
    }

    var showDialog by remember { mutableStateOf(false) }

    val safeSubtitle = if (state.value >= 0 && useSelectedValueAsSubtitle) {
        { Text(text = items[state.value]) }
    } else {
        subtitle
    }

    SettingsMenuLink(
        modifier = modifier,
        icon = icon,
        title = title,
        subtitle = safeSubtitle,
        action = action,
        onClick = { showDialog = true },
    )

    if (!showDialog) return

    val coroutineScope = rememberCoroutineScope()
    val onSelected: (Int) -> Unit = { selectedIndex ->
        coroutineScope.launch {
            state.value = selectedIndex
            delay(closeDialogDelay)
            showDialog = false
        }
    }

    AlertDialog(
        title = title,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
            ) {
                if (subtitle != null) {
                    subtitle()
                    Spacer(modifier = Modifier.size(8.dp))
                }

                items.forEachIndexed { index, item ->
                    val isSelected by rememberUpdatedState(newValue = state.value == index)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                role = Role.RadioButton,
                                selected = isSelected,
                                onClick = { if (!isSelected) onSelected(index) }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = null
                        )
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        onDismissRequest = { showDialog = false },
        confirmButton = {},
        dismissButton = {},
    )
}