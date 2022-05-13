package top.gangshanghua.xiaobo.lib_simple.loading

import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import top.gangshanghua.xiaobo.lib_simple.helper.SimpleLoadingDialog

interface Loading {

    companion object {
        // we must use a tag to find the specified fragment.
        // you can not hold an instance of DialogFragment to call dialog.show()/dialog.hide().
        // for if you rotate the screen, then activity will be re-created, then you will
        // hold another new instance of DialogFragment, at this time you call hide() without show()
        // called before, exception will be thrown.
        // eg: Activity#onCreate -> dialog=DialogFragment() -> dialog.show() ->
        //     Activity#onCreate -> dialog=DialogFragment() -> dialog.hide() -> crash
        private const val TAG_LOADING_DIALOG_FRAGMENT = "TAG_LOADING_DIALOG_FRAGMENT"
    }

    var mLoading: Int
    val mHandler: Handler

    fun handleLoading(show: Boolean) {
        if (show) {
            mHandler.post {
                showLoading()
            }
        } else {
            mHandler.post {
                hideLoading()
            }
        }
    }

    private fun showLoading() {
        if (mLoading++ > 0) {
            return
        }

        if (this is AppCompatActivity) {
            val fragment = supportFragmentManager.findFragmentByTag(TAG_LOADING_DIALOG_FRAGMENT)
            if (fragment == null) {
                SimpleLoadingDialog.newInstance()
                    .show(supportFragmentManager, TAG_LOADING_DIALOG_FRAGMENT)
            }
        }
    }

    private fun hideLoading() {
        if (mLoading-- > 1) {
            return
        }

        if (this is AppCompatActivity) {
            supportFragmentManager.findFragmentByTag(TAG_LOADING_DIALOG_FRAGMENT)?.apply {
                (this as DialogFragment).dismiss()
            }
        }
    }

}