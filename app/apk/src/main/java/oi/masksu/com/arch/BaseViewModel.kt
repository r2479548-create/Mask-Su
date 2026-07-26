package oi.masksu.com.arch

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.REQUEST_INSTALL_PACKAGES
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import oi.masksu.com.core.R
import oi.masksu.com.events.BackPressEvent
import oi.masksu.com.events.DialogBuilder
import oi.masksu.com.events.DialogEvent
import oi.masksu.com.events.PermissionEvent
import oi.masksu.com.utils.TextHolder
import oi.masksu.com.utils.asText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import top.yukonga.miuix.kmp.basic.SnackbarDuration

abstract class BaseViewModel : ViewModel() {

    sealed interface BaseEvent {
        data class ShowSnackbar(
            val message: TextHolder,
            val duration: SnackbarDuration = SnackbarDuration.Short,
            val actionLabel: String? = null,
            val onActionPerformed: (() -> Unit)? = null,
        ) : BaseEvent
    }

    private val _uiEvents = MutableLiveData<UiEvent>()
    val uiEvents: LiveData<UiEvent> get() = _uiEvents

    private val _baseEvent = Channel<BaseEvent>(Channel.BUFFERED)
    val baseEvent: Flow<BaseEvent> = _baseEvent.receiveAsFlow()

    fun sendBaseEvent(event: BaseEvent) {
        _baseEvent.trySend(event)
    }

    open fun onSaveState(state: Bundle) {}
    open fun onRestoreState(state: Bundle) {}
    open fun onNetworkChanged(network: Boolean) {}

    fun withPermission(permission: String, callback: (Boolean) -> Unit) {
        PermissionEvent(permission, callback).publish()
    }

    inline fun withExternalRW(crossinline callback: () -> Unit) {
        withPermission(WRITE_EXTERNAL_STORAGE) {
            if (!it) {
                sendBaseEvent(BaseEvent.ShowSnackbar(R.string.external_rw_permission_denied.asText()))
            } else {
                callback()
            }
        }
    }

    @SuppressLint("InlinedApi")
    inline fun withInstallPermission(crossinline callback: () -> Unit) {
        withPermission(REQUEST_INSTALL_PACKAGES) {
            if (!it) {
                sendBaseEvent(BaseEvent.ShowSnackbar(R.string.install_unknown_denied.asText()))
            } else {
                callback()
            }
        }
    }

    @SuppressLint("InlinedApi")
    inline fun withPostNotificationPermission(crossinline callback: () -> Unit) {
        withPermission(POST_NOTIFICATIONS) {
            if (!it) {
                sendBaseEvent(BaseEvent.ShowSnackbar(R.string.post_notifications_denied.asText()))
            } else {
                callback()
            }
        }
    }

    fun back() = BackPressEvent().publish()

    fun UiEvent.publish() {
        _uiEvents.postValue(this)
    }

    fun DialogBuilder.show() {
        DialogEvent(this).publish()
    }
}
