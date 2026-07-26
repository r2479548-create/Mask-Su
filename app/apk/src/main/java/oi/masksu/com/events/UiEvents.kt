package oi.masksu.com.events

import android.content.Context
import androidx.activity.ComponentActivity
import oi.masksu.com.arch.ActivityExecutor
import oi.masksu.com.arch.ContextExecutor
import oi.masksu.com.arch.UiEvent
import oi.masksu.com.core.base.ContentResultCallback
import oi.masksu.com.core.base.IActivityExtension
import oi.masksu.com.core.base.relaunch
import oi.masksu.com.core.integration.AppShortcuts
import oi.masksu.com.ui.dialog.MaskSuDialog

class PermissionEvent(
    private val permission: String,
    private val callback: (Boolean) -> Unit
) : UiEvent(), ActivityExecutor {

    override fun invoke(activity: ComponentActivity) =
        (activity as? IActivityExtension)?.withPermission(permission, callback) ?: callback(false)
}

class BackPressEvent : UiEvent(), ActivityExecutor {
    override fun invoke(activity: ComponentActivity) {
        activity.onBackPressedDispatcher.onBackPressed()
    }
}

class DieEvent : UiEvent(), ActivityExecutor {
    override fun invoke(activity: ComponentActivity) {
        activity.finish()
    }
}

class RecreateEvent : UiEvent(), ActivityExecutor {
    override fun invoke(activity: ComponentActivity) {
        activity.relaunch()
    }
}

class AuthEvent(
    private val callback: () -> Unit
) : UiEvent(), ActivityExecutor {

    override fun invoke(activity: ComponentActivity) {
        (activity as? IActivityExtension)?.withAuthentication { if (it) callback() }
    }
}

class GetContentEvent(
    private val type: String,
    private val callback: ContentResultCallback
) : UiEvent(), ActivityExecutor {
    override fun invoke(activity: ComponentActivity) {
        (activity as? IActivityExtension)?.getContent(type, callback)
    }
}

class AddHomeIconEvent : UiEvent(), ContextExecutor {
    override fun invoke(context: Context) {
        AppShortcuts.addHomeIcon(context)
    }
}

class DialogEvent(
    private val builder: DialogBuilder
) : UiEvent(), ActivityExecutor {
    override fun invoke(activity: ComponentActivity) {
        MaskSuDialog(activity).apply(builder::build).show()
    }
}

interface DialogBuilder {
    fun build(dialog: MaskSuDialog)
}
