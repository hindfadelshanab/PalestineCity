package developer.citypalestine8936ps.utilites

import android.content.Context
import android.graphics.Color
import taimoor.sultani.sweetalert2.Sweetalert


data class DialogUtil(val context: Context) {
    private val loadingDialog = Sweetalert(context, Sweetalert.PROGRESS_TYPE)
    fun showLoadingDialog(status: Boolean, title: String = "Loading") {
        if (status) {
            if (loadingDialog.isShowing)
                loadingDialog.dismiss()
            loadingDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
            loadingDialog.titleText = title
            loadingDialog.setCancelable(false)
            loadingDialog.show()
        } else {
            if (loadingDialog.isShowing)
                loadingDialog.dismiss()
        }
    }
}