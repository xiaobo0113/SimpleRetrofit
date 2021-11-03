package top.gangshanghua.xiaobo.simpleretrofit

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.blankj.utilcode.util.CrashUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import top.gangshanghua.xiaobo.simpleretrofit.base.EmptyActivityLifecycleCallbacks
import top.gangshanghua.xiaobo.simpleretrofit.base.LoadingBaseActivity
import top.gangshanghua.xiaobo.simpleretrofit.simple.SimpleApi
import java.util.concurrent.ConcurrentHashMap

class MainApplication : Application() {

    companion object {
        private lateinit var sAppContext: Context
    }

    private val mCountMap = ConcurrentHashMap<String, Int>()
    private val mActivityMap = ConcurrentHashMap<String, LoadingBaseActivity>()

    override fun onCreate() {
        super.onCreate()
        sAppContext = this

        initCrash()
        initLog()
        initLoading()
        SimpleApi.init()
    }

    private fun initLoading() {
        SimpleApi.mLoadingLiveData.observeForever {
            if (mCountMap[it.second] == null) {
                mCountMap[it.second] = 0
            }

            if (it.first) {
                if (mCountMap[it.second] == 0) {
                    mActivityMap[it.second]?.showLoading()
                }
                mCountMap[it.second] = mCountMap[it.second]!! + 1

            } else {
                mCountMap[it.second] = mCountMap[it.second]!! - 1
                if (mCountMap[it.second] == 0) {
                    mCountMap.remove(it.second)
                    mActivityMap[it.second]?.hideLoading()
                }
            }
        }

        registerActivityLifecycleCallbacks(object : EmptyActivityLifecycleCallbacks() {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (activity is LoadingBaseActivity) {
                    mActivityMap[activity.mUUID] = activity
                }
            }

            override fun onActivityDestroyed(activity: Activity) {
                if (activity is LoadingBaseActivity) {
                    mActivityMap.remove(activity.mUUID)
                }
            }
        })
    }

    private fun initLog() {
        val strArray = Utils.getApp().packageName.split("\\.").toTypedArray()
        val filePrefix = strArray[strArray.size - 1]
        val config = LogUtils.getConfig()
        config.isLog2FileSwitch = true
        config.filePrefix = filePrefix
        config.saveDays = 30
        config.globalTag = "hehe"
    }

    private fun initCrash() {
        CrashUtils.init()
    }

}