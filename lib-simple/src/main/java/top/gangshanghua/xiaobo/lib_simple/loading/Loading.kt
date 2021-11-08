package top.gangshanghua.xiaobo.lib_simple.loading

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

    /**
     * you are responsible to save and restore the uuid for activity configuration change.
     * that is to say, if a configuration change leads to the re-construction of activity,
     * you must guarantee the uuid is the same before the re-construction.
     */
    fun getUUID(): String

    fun showLoading() {
        if (this is AppCompatActivity) {
            val fragment = supportFragmentManager.findFragmentByTag(TAG_LOADING_DIALOG_FRAGMENT)
            if (fragment == null) {
                SimpleLoadingDialog.newInstance()
                    .show(supportFragmentManager, TAG_LOADING_DIALOG_FRAGMENT)
            }
        }
    }

    fun hideLoading() {
        if (this is AppCompatActivity) {
            supportFragmentManager.findFragmentByTag(TAG_LOADING_DIALOG_FRAGMENT)?.apply {
                (this as DialogFragment).dismiss()
            }
        }
    }

}