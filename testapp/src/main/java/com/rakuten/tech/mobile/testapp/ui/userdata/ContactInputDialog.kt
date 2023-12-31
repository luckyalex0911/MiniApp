package com.rakuten.tech.mobile.testapp.ui.userdata

import android.app.AlertDialog
import android.content.Context
import android.view.View

class ContactInputDialog {

    class Builder {
        var alert: AlertDialog.Builder? = null
        var positiveListener: View.OnClickListener? = null
        var dialog: AlertDialog? = null

        fun build(context: Context?): Builder {
            alert = AlertDialog.Builder(context)
            setNegativeListener()
            return this
        }

        fun setView(view: View): Builder {
            alert?.setView(view)
            return this
        }

        fun setPositiveListener(listener: View.OnClickListener): Builder {
            positiveListener = listener
            return this
        }

        fun setPositiveButton(title: String){
            alert?.setPositiveButton(title, null)
        }

        fun setDialogTitle(title: String){
            alert?.setTitle(title)
        }

        private fun setNegativeListener(): Builder {
            alert?.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            return this
        }

        fun show() {
            dialog = alert?.create()

            dialog?.setOnShowListener {
                dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener(positiveListener)
            }

            if (dialog != null && !dialog!!.isShowing)
                dialog!!.show()
        }
    }
}
