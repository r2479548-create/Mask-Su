package oi.masksu.com.dialog

import oi.masksu.com.core.R
import oi.masksu.com.events.DialogBuilder
import oi.masksu.com.ui.dialog.WeaveDialog

class SecondSlotWarningDialog : DialogBuilder {

    override fun build(dialog: WeaveDialog) {
        dialog.apply {
            setTitle(android.R.string.dialog_alert_title)
            setMessage(R.string.install_inactive_slot_msg)
            setButton(WeaveDialog.ButtonType.POSITIVE) {
                text = android.R.string.ok
            }
            setCancelable(true)
        }
    }
}
