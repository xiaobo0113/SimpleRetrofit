package top.gangshanghua.xiaobo.simpleretrofit.base

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import okhttp3.Call
import top.gangshanghua.xiaobo.simpleretrofit.simple.SimpleApi

abstract class LoadingBaseActivity : BaseActivity() {

    companion object {
        const val EXTRA_STARTED_TIME = "EXTRA_STARTED_TIME"
    }

    protected var mStartedTime = ""

    private lateinit var mProgressBar: ProgressBar
    private var mCount = 0
    private val mLoadingObserver = Observer<Triple<Boolean, String, Call>> {
        if (mStartedTime != it.second) {
            return@Observer
        }

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

    open fun forbidBackPressWhenLoading() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO maybe use uuid
        mStartedTime = "${System.currentTimeMillis()}"
        intent.putExtra(EXTRA_STARTED_TIME, mStartedTime)

        (window.decorView as ViewGroup).addView(ProgressBar(this).apply {
            mProgressBar = this
            setBackgroundColor(Color.parseColor("#7f000000"))
        }, -1)
        mProgressBar.isVisible = false

        onBackPressedDispatcher.addCallback(this, forbidBackPressWhenLoading()) {
            if (!mProgressBar.isVisible) {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            } else {
                // wait network to complete
            }
        }

        SimpleApi.mLoadingLiveData.observeForever(mLoadingObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        SimpleApi.mLoadingLiveData.removeObserver(mLoadingObserver)
    }

}