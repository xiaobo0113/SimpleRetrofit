package top.gangshanghua.xiaobo.simpleretrofit

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.activity.addCallback
import androidx.core.view.isVisible
import com.blankj.utilcode.util.LogUtils
import top.gangshanghua.xiaobo.simpleretrofit.simple.SimpleApi

abstract class LoadingBaseActivity : BaseActivity() {

    private lateinit var mProgressBar: ProgressBar
    private var mCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (window.decorView as ViewGroup).addView(ProgressBar(this).apply {
            mProgressBar = this
            setBackgroundColor(Color.parseColor("#7f000000"))
        }, -1)
        mProgressBar.isVisible = false

        onBackPressedDispatcher.addCallback(this) {
            if (!mProgressBar.isVisible) {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            } else {
                // wait network to complete
            }
        }

        SimpleApi.mLoadingLiveData.observe(this) {
            LogUtils.d("mCount before: $mCount")

            if (it.first) {
                if (mCount++ == 0) {
                    mProgressBar.isVisible = true
                }
            } else {
                if (--mCount == 0) {
                    mProgressBar.isVisible = false
                }
            }

            LogUtils.d("mCount after: $mCount")
        }
    }

}