package oi.masksu.com.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import oi.masksu.com.core.Config
import oi.masksu.com.core.R as CoreR
import oi.masksu.com.ui.module.RebootListPopup
import oi.masksu.com.ui.theme.LocalEnableBlur
import oi.masksu.com.ui.theme.LocalHomeLayoutMode
import oi.masksu.com.ui.util.attachBarBlurBackdrop
import oi.masksu.com.ui.util.barBlurContainerColor
import oi.masksu.com.ui.util.defaultBarBlur
import oi.masksu.com.ui.util.rememberBarBlurBackdrop
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PopupPositionProvider
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.theme.MiuixTheme
import oi.masksu.com.arch.BaseViewModel.BaseEvent
import oi.masksu.com.ui.component.ObserveAsEvents
import oi.masksu.com.ui.component.showSnackbarEvent
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    contentBottomPadding: Dp,
    onNavigateToInstall: () -> Unit,
    onNavigateToUninstall: () -> Unit,
    onNavigateToAbout: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val homeLayoutMode = LocalHomeLayoutMode.current
    val isMaskSuHome = homeLayoutMode == Config.Value.HOME_LAYOUT_MASKSU
    var hasStartedLoading by rememberSaveable { mutableStateOf(false) }
    var isMagiskCardExpanded by rememberSaveable { mutableStateOf(false) }
    var isManagerCardExpanded by rememberSaveable { mutableStateOf(false) }
    var isSupportCardExpanded by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = MiuixScrollBehavior()
    val enableBlur = LocalEnableBlur.current
    val surfaceColor = MiuixTheme.colorScheme.surface
    val blurBackdrop = rememberBarBlurBackdrop(enableBlur, surfaceColor)
    val cardActionEnter = fadeIn(
        animationSpec = tween(durationMillis = 220)
    ) + expandVertically(
        expandFrom = Alignment.Top,
        animationSpec = tween(durationMillis = 260)
    )
    val cardActionExit = fadeOut(
        animationSpec = tween(durationMillis = 180)
    ) + shrinkVertically(
        shrinkTowards = Alignment.Top,
        animationSpec = tween(durationMillis = 220)
    )

    LaunchedEffect(configuration) {
        viewModel.dismissManagerInstallDialog()
    }

    LaunchedEffect(viewModel.appState) {
        if (viewModel.appState == HomeViewModel.State.LOADING ||
            viewModel.appState == HomeViewModel.State.INVALID
        ) {
            isManagerCardExpanded = false
        }
    }

    LaunchedEffect(hasStartedLoading) {
        if (!hasStartedLoading) {
            hasStartedLoading = true
            viewModel.startLoading()
        }
    }

    val scope = rememberCoroutineScope()
    ObserveAsEvents(viewModel.event) { event ->
        when (event) {
            is HomeViewModel.HomeEvent.ShowSnackbar -> scope.launch {
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

    HomeDialogHost(
        viewModel = viewModel,
        onNavigateToInstall = onNavigateToInstall,
        onNavigateToUninstall = onNavigateToUninstall
    )

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.systemBars.add(WindowInsets.displayCutout).only(WindowInsetsSides.Horizontal),
        topBar = {
            TopAppBar(
                modifier = Modifier.defaultBarBlur(blurBackdrop, surfaceColor),
                color = barBlurContainerColor(blurBackdrop, surfaceColor),
                title = context.getString(CoreR.string.section_home),
                scrollBehavior = scrollBehavior,
                actions = {
                    RebootListPopup(
                        alignment = PopupPositionProvider.Align.TopEnd,
                    )
                }
            )
        },
        popupHost = { }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .attachBarBlurBackdrop(blurBackdrop),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .scrollEndHaptic()
                    .overScrollVertical()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .padding(horizontal = 12.dp),
                contentPadding = innerPadding,
                overscrollEffect = null
            ) {
                item {
                    AnimatedVisibility(
                        visible = viewModel.isNoticeVisible,
                        exit = shrinkVertically(
                            animationSpec = tween(durationMillis = 300)
                        ) + fadeOut(
                            animationSpec = tween(durationMillis = 300)
                        )
                    ) {
                        NoticeCard(
                            onHide = { viewModel.hideNotice() }
                        )
                    }
                }

                if (isMaskSuHome) {
                    masksuHomeContent(
                        viewModel = viewModel,
                        magiskInstalledVersion = viewModel.magiskInstalledVersion.getText(context.resources),
                        isMagiskCardExpanded = isMagiskCardExpanded,
                        onMagiskExpandedChange = { isMagiskCardExpanded = it },
                        isSupportCardExpanded = isSupportCardExpanded,
                        onSupportExpandedChange = { isSupportCardExpanded = it },
                        onNavigateToInstall = onNavigateToInstall,
                        onNavigateToAbout = onNavigateToAbout,
                        cardActionEnter = cardActionEnter,
                        cardActionExit = cardActionExit
                    )
                } else {
                    classicHomeContent(
                        viewModel = viewModel,
                        magiskInstalledVersion = viewModel.magiskInstalledVersion.getText(context.resources),
                        managerRemoteVersion = viewModel.managerRemoteVersion.getText(context.resources),
                        isMagiskCardExpanded = isMagiskCardExpanded,
                        onMagiskExpandedChange = { isMagiskCardExpanded = it },
                        isManagerCardExpanded = isManagerCardExpanded,
                        onManagerExpandedChange = { isManagerCardExpanded = it },
                        onNavigateToInstall = onNavigateToInstall,
                        onNavigateToAbout = onNavigateToAbout,
                        cardActionEnter = cardActionEnter,
                        cardActionExit = cardActionExit
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(contentBottomPadding))
                }
            }
        }
    }
}
