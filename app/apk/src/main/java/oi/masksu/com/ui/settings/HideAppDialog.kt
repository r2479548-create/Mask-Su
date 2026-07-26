package oi.masksu.com.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import oi.masksu.com.core.R as CoreR
import oi.masksu.com.ui.component.MiuixLoadingDialog
import oi.masksu.com.ui.component.MiuixTextInputDialog

internal const val HideAppNameMaxLength = 30

@Composable
fun HideAppDialog(
    show: Boolean,
    appName: TextFieldValue,
    onAppNameChange: (TextFieldValue) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    MiuixTextInputDialog(
        show = show,
        title = stringResource(CoreR.string.settings_hide_app_title),
        summary = stringResource(CoreR.string.settings_hide_app_summary),
        value = appName,
        onValueChange = {
            if (it.text.length <= HideAppNameMaxLength) {
                onAppNameChange(it)
            }
        },
        label = stringResource(CoreR.string.settings_app_name_hint),
        helperText = stringResource(CoreR.string.settings_app_name_helper),
        counterText = "${appName.text.length}/$HideAppNameMaxLength",
        confirmText = stringResource(android.R.string.ok),
        dismissText = stringResource(android.R.string.cancel),
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        confirmEnabled = appName.text.isNotBlank(),
    )
}

@Composable
fun HideAppLoadingDialog(
    show: Boolean,
    title: String,
) {
    MiuixLoadingDialog(show = show, title = title)
}