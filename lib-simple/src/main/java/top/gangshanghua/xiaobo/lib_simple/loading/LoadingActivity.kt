package top.gangshanghua.xiaobo.lib_simple.loading

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import java.util.*

abstract class LoadingActivity : AppCompatActivity(), Loading {

    private lateinit var mUUID: String

    companion object {
        private const val KEY_UUID = "KEY_UUID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mUUID = if (null == savedInstanceState) {
            UUID.randomUUID().toString()
        } else {
            savedInstanceState.getString(KEY_UUID, "")
        }
        LogUtils.d("mUUID: $mUUID")
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_UUID, mUUID)
    }

    override fun getUUID(): String {
        return mUUID
    }

}