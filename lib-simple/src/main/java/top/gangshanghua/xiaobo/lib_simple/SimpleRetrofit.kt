package top.gangshanghua.xiaobo.lib_simple

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.OkHttpClient
import top.gangshanghua.xiaobo.lib_simple.helper.EmptyActivityLifecycleCallbacks
import top.gangshanghua.xiaobo.lib_simple.loading.Loading
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

object SimpleRetrofit {

    private val mCallMap = ConcurrentHashMap<Call, Loading>()
    private val mActivityList = CopyOnWriteArrayList<Loading>()

    fun init(context: Context, builder: OkHttpClient.Builder): OkHttpClient.Builder {
        (context.applicationContext as Application).registerActivityLifecycleCallbacks(
            object : EmptyActivityLifecycleCallbacks() {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    if (activity is Loading) {
                        mActivityList.add(activity)
                    }
                }

                override fun onActivityDestroyed(activity: Activity) {
                    if (activity is Loading) {
                        mActivityList.remove(activity)
                    }
                }
            })

        builder.eventListener(object : EventListener() {
            override fun callStart(call: Call) {
                super.callStart(call)
                mActivityList.lastOrNull()?.let {
                    mCallMap[call] = it
                }
                handleLoading(true, call)
            }

            override fun callEnd(call: Call) {
                super.callEnd(call)
                handleLoading(false, call)
                mCallMap.remove(call)
            }

            override fun callFailed(call: Call, ioe: IOException) {
                super.callFailed(call, ioe)
                handleLoading(false, call)
                mCallMap.remove(call)
            }
        })

        return builder
    }

    private fun handleLoading(show: Boolean, call: Call) {
        mCallMap[call]?.handleLoading(show)
    }

}