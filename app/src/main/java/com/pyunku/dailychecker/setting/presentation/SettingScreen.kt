package com.pyunku.dailychecker.setting

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alorma.compose.settings.storage.base.SettingValueState
import com.alorma.compose.settings.storage.base.rememberIntSettingState
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.pyunku.dailychecker.R
import com.pyunku.dailychecker.data.CheckShape
import com.pyunku.dailychecker.setting.presentation.SettingMenuLink
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val userPreferences by viewModel.userPreferencesState.collectAsState()

    Column() {
        SettingList(
            title = { Text(text = stringResource(R.string.setting_check_shape)) },
            subtitle = { Text(text = stringResource(R.string.setting_select_check_shape)) },
            items = CheckShape.values().map { it.name },
            onSelectedAction = { selectedIndex ->
                viewModel.setCheckShape(
                    CheckShape.values().first {
                        it.ordinal == selectedIndex
                    }
                )
            },
            icon = {
                Image(
                    painter = painterResource(id = userPreferences.checkShape.resId),
                    contentDescription = ("check shape"),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            }
        )
        Divider()
        SettingsMenuLink(
            title = { Text(text = stringResource(R.string.License)) },
            onClick = {
                context.startActivity(Intent(context,
                    OssLicensesMenuActivity::class.java))
            },
        )
        Divider()
    }
}

@Composable
fun SettingList(
    modifier: Modifier = Modifier,
    state: SettingValueState<Int> = rememberIntSettingState(),
    title: @Composable () -> Unit,
    items: List<String>,
    icon: (@Composable () -> Unit)? = null,
    useSelectedValueAsSubtitle: Boolean = true,
    subtitle: (@Composable () -> Unit)? = null,
    closeDialogDelay: Long = 200,
    action: (@Composable () -> Unit)? = null,
    onSelectedAction: ((Int) -> Unit),
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

    SettingMenuLink(
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
        onSelectedAction(selectedIndex)
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
                        Image(
                            modifier = Modifier.padding(start = 16.dp),
                            painter = painterResource(id = CheckShape.valueOf(item).resId),
                            contentDescription = ("check shape"),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
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
