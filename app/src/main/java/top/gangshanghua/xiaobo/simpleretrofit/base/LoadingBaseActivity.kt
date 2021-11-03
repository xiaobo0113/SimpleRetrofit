package top.gangshanghua.xiaobo.simpleretrofit.base

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.blankj.utilcode.util.LogUtils
import top.gangshanghua.xiaobo.simpleretrofit.simple.SimpleLoadingDialog
import java.util.*

abstract class LoadingBaseActivity : BaseActivity() {

    companion object {
        private const val KEY_STARTED_TIME = "KEY_STARTED_TIME"

        // we must use a tag to find the specified fragment.
        // you can not hold an instance of DialogFragment to call dialog.show()/dialog.hide().
        // for if you rotate the screen, then activity will be re-created, then you will
        // hold another new instance of DialogFragment, at this time you call hide() without show()
        // called before, exception will be thrown.
        // eg: Activity#onCreate -> dialog=DialogFragment() -> dialog.show() ->
        //     Activity#onCreate -> dialog=DialogFragment() -> dialog.hide() -> crash
        private const val TAG_LOADING_DIALOG_FRAGMENT = "TAG_LOADING_DIALOG_FRAGMENT"
    }

    lateinit var mUUID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        mUUID = if (null == savedInstanceState) {
            UUID.randomUUID().toString()
        } else {
            savedInstanceState.getString(KEY_STARTED_TIME, "")
        }
        LogUtils.d("mUUID: $mUUID")

        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_STARTED_TIME, mUUID)
    }

    fun showLoading() {
        val fragment = supportFragmentManager.findFragmentByTag(TAG_LOADING_DIALOG_FRAGMENT)
        if (fragment == null) {
            SimpleLoadingDialog.newInstance()
                .show(supportFragmentManager, TAG_LOADING_DIALOG_FRAGMENT)
        }
    }

    fun hideLoading() {
        supportFragmentManager.findFragmentByTag(TAG_LOADING_DIALOG_FRAGMENT)?.apply {
            (this as DialogFragment).dismiss()
        }
    }

}