package oi.masksu.com.ui.module

import android.net.Uri
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import oi.masksu.com.core.R as CoreR
import oi.masksu.com.ui.MainActivity
import oi.masksu.com.ui.component.SearchStatus
import oi.masksu.com.ui.module.components.ModuleScreenContent
import oi.masksu.com.ui.module.components.ModuleScreenTopBar
import oi.masksu.com.ui.module.components.ModuleSearchResultsHost
import oi.masksu.com.ui.module.dialogs.ModuleScreenDialogs
import oi.masksu.com.ui.module.state.rememberLocalModulePicker
import oi.masksu.com.ui.module.state.rememberModuleScreenLocalState
import oi.masksu.com.ui.module.state.rememberShortcutIconPicker
import oi.masksu.com.ui.theme.LocalEnableBlur
import oi.masksu.com.ui.util.rememberBarBlurBackdrop
import oi.masksu.com.arch.BaseViewModel.BaseEvent
import oi.masksu.com.ui.component.ObserveAsEvents
import oi.masksu.com.ui.component.showSnackbarEvent
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.theme.MiuixTheme
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun ModuleScreen(
    viewModel: ModuleViewModel,
    contentBottomPadding: Dp,
    onInstallModuleFromLocal: (List<Uri>) -> Unit,
    onOpenRepo: () -> Unit,
    onRunAction: (String, String) -> Unit,
    onOpenWebUi: (String, String) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val uiActivity = context as? MainActivity
    val uiState = viewModel.uiState
    val searchModulesLabel = stringResource(CoreR.string.search_modules_label)
    val localState = rememberModuleScreenLocalState(searchModulesLabel)
    val shortcutState = rememberModuleShortcutState(context)
    val launchLocalModulePicker = rememberLocalModulePicker(
        onModulePicked = viewModel::requestInstallLocalModule,
    )
    val launchShortcutIconPicker = rememberShortcutIconPicker(
        onIconPicked = shortcutState::updateIconUri,
    )
    val enableBlur = LocalEnableBlur.current
    val blurBackdrop = rememberBarBlurBackdrop(enableBlur, MiuixTheme.colorScheme.surface)
    val scrollBehavior = MiuixScrollBehavior()
    val uiSearchStatus = localState.searchStatus.copy(
        resultStatus = when {
            uiState.isLoading -> SearchStatus.ResultStatus.LOAD
            uiState.modules.isEmpty() -> SearchStatus.ResultStatus.EMPTY
            else -> SearchStatus.ResultStatus.SHOW
        }
    )

    fun onModuleAddShortcut(module: ModuleInfo) {
        shortcutState.bindModule(module)
        when (shortcutState.availableTypes.size) {
            0 -> Unit
            1 -> {
                shortcutState.selectType(shortcutState.availableTypes.first())
                localState.showShortcutDialog = true
            }

            else -> {
                localState.showShortcutTypeDialog = true
            }
        }
    }

    DisposableEffect(onRunAction) {
        viewModel.onRunAction = onRunAction
        onDispose {
            viewModel.onRunAction = null
        }
    }

    LaunchedEffect(Unit) {
        viewModel.initializePreferences()
        viewModel.startLoading()
    }

    LaunchedEffect(localState.searchStatus.searchText) {
        if (uiState.query != localState.searchStatus.searchText) {
            viewModel.setQuery(localState.searchStatus.searchText)
        }
    }

    val scope = rememberCoroutineScope()
    ObserveAsEvents(viewModel.event) { event ->
        when (event) {
            is ModuleViewModel.ModuleEvent.ShowSnackbar -> scope.launch {
                snackbarHostState.showSnackbarEvent(
                    message = event.message.getText(context.resources),
                    duration = event.duration,
                )
            }
        }
    }
    ObserveAsEvents(viewModel.baseEvent) { event ->
        when (event) {
            is BaseEvent.ShowSnackbar -> scope.launch {
                snackbarHostState.showSnackbarEvent(
                    message = event.message.getText(context.resources),
                    duration = event.duration,
                )
            }
        }
    }

    MiuixTheme {
        ModuleScreenDialogs(
            uiActivity = uiActivity,
            onlineInstallDialogState = viewModel.onlineInstallDialogState,
            localInstallDialogState = viewModel.localInstallDialogState,
            shortcutState = shortcutState,
            showShortcutDialog = localState.showShortcutDialog,
            showShortcutTypeDialog = localState.showShortcutTypeDialog,
            onDismissOnlineInstallDialog = viewModel::dismissOnlineInstallDialog,
            onDismissLocalInstallDialog = viewModel::dismissLocalInstallDialog,
            onConfirmLocalInstall = onInstallModuleFromLocal,
            onDismissShortcutTypeDialog = { localState.showShortcutTypeDialog = false },
            onSelectShortcutType = { type ->
                localState.showShortcutTypeDialog = false
                shortcutState.selectType(type)
                localState.showShortcutDialog = true
            },
            onDismissShortcutDialog = { localState.showShortcutDialog = false },
            onPickShortcutIcon = launchShortcutIconPicker,
        )

        Scaffold(
            modifier = modifier,
            popupHost = {
                ModuleSearchResultsHost(
                    uiSearchStatus = uiSearchStatus,
                    modules = uiState.modules,
                    contentBottomPadding = contentBottomPadding,
                    onSearchStatusChange = { localState.searchStatus = it },
                    onToggleModule = { module, enabled -> viewModel.toggleModule(module.id, enabled) },
                    onRunAction = { module -> viewModel.runAction(module.id, module.name) },
                    onOpenWebUi = { module -> onOpenWebUi(module.id, module.name) },
                    onAddShortcut = ::onModuleAddShortcut,
                    onDownloadUpdate = { module -> viewModel.downloadPressed(module.updateInfo) },
                    onToggleModuleRemove = { module -> viewModel.toggleModuleRemove(module.id) },
                )
            },
            contentWindowInsets = WindowInsets.systemBars
                .add(WindowInsets.displayCutout)
                .only(WindowInsetsSides.Horizontal),
            topBar = {
                ModuleScreenTopBar(
                    uiSearchStatus = uiSearchStatus,
                    uiState = uiState,
                    blurBackdrop = blurBackdrop,
                    scrollBehavior = scrollBehavior,
                    onSearchStatusChange = { localState.searchStatus = it },
                    showTopPopup = localState.showTopPopup,
                    onShowTopPopupChange = { localState.showTopPopup = it },
                    onOpenRepo = onOpenRepo,
                    onToggleSortEnabledFirst = viewModel::toggleSortEnabledFirst,
                    onToggleSortUpdateFirst = viewModel::toggleSortUpdateFirst,
                    onToggleSortExecutableFirst = viewModel::toggleSortExecutableFirst,
                )
            },
            content = { innerPadding ->
                ModuleScreenContent(
                    innerPadding = innerPadding,
                    uiState = uiState,
                    uiSearchStatus = uiSearchStatus,
                    contentBottomPadding = contentBottomPadding,
                    blurBackdrop = blurBackdrop,
                    scrollBehavior = scrollBehavior,
                    onSearchStatusChange = { localState.searchStatus = it },
                    onRefresh = viewModel::refresh,
                    onInstallPressed = launchLocalModulePicker,
                    onToggleModule = { module, enabled -> viewModel.toggleModule(module.id, enabled) },
                    onRunAction = { module -> viewModel.runAction(module.id, module.name) },
                    onOpenWebUi = { module -> onOpenWebUi(module.id, module.name) },
                    onAddShortcut = ::onModuleAddShortcut,
                    onDownloadUpdate = { module -> viewModel.downloadPressed(module.updateInfo) },
                    onToggleModuleRemove = { module -> viewModel.toggleModuleRemove(module.id) },
                )
            },
        )
    }
}
