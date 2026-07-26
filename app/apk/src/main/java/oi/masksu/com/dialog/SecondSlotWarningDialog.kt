package oi.masksu.com.dialog

import oi.masksu.com.core.R
import oi.masksu.com.events.DialogBuilder
import oi.masksu.com.ui.dialog.MaskSuDialog

class SecondSlotWarningDialog : DialogBuilder {

    override fun build(dialog: MaskSuDialog) {
        dialog.apply {
            setTitle(android.R.string.dialog_alert_title)
            setMessage(R.string.install_inactive_slot_msg)
            setButton(MaskSuDialog.ButtonType.POSITIVE) {
                text = android.R.string.ok
            }
            setCancelable(true)
        }
    }
}
