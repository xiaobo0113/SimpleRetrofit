package top.gangshanghua.xiaobo.lib_simple

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.OkHttpClient
import top.gangshanghua.xiaobo.lib_simple.helper.EmptyActivityLifecycleCallbacks
import top.gangshanghua.xiaobo.lib_simple.loading.Loading
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

object SimpleRetrofit {

    const val HEADER_UUID = "HEADER_UUID"

    private val mHandler = Handler(Looper.getMainLooper())
    private val mCountMap = ConcurrentHashMap<String, Int>()
    private val mActivityMap = ConcurrentHashMap<String, Loading>()

    fun init(context: Context, builder: OkHttpClient.Builder): OkHttpClient {
        (context.applicationContext as Application).registerActivityLifecycleCallbacks(
            object : EmptyActivityLifecycleCallbacks() {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    if (activity is Loading) {
                        mActivityMap[activity.getUUID()] = activity
                    }
                }

                override fun onActivityDestroyed(activity: Activity) {
                    if (activity is Loading) {
                        mActivityMap.remove(activity.getUUID())
                    }
                }
            })

        builder.eventListener(object : EventListener() {
            override fun callStart(call: Call) {
                super.callStart(call)
                // can not use postValue, for maybe only the last value will be sent
                call.request().header(HEADER_UUID)?.let {
                    mHandler.post {
                        handleLoading(true, it)
                    }
                }
            }

            override fun callEnd(call: Call) {
                super.callEnd(call)
                call.request().header(HEADER_UUID)?.let {
                    mHandler.post {
                        handleLoading(false, it)
                    }
                }
            }

            override fun callFailed(call: Call, ioe: IOException) {
                super.callFailed(call, ioe)
                call.request().header(HEADER_UUID)?.let {
                    mHandler.post {
                        handleLoading(false, it)
                    }
                }
            }
        })

        return builder.build()
    }

    private fun handleLoading(show: Boolean, uuid: String) {
        if (mCountMap[uuid] == null) {
            mCountMap[uuid] = 0
        }

        if (show) {
            if (mCountMap[uuid] == 0) {
                mActivityMap[uuid]?.showLoading()
            }
            mCountMap[uuid] = mCountMap[uuid]!! + 1

        } else {
            mCountMap[uuid] = mCountMap[uuid]!! - 1
            if (mCountMap[uuid] == 0) {
                mCountMap.remove(uuid)
                mActivityMap[uuid]?.hideLoading()
            }
        }
    }

}