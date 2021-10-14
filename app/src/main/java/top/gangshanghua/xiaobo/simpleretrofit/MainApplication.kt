package top.gangshanghua.xiaobo.simpleretrofit

import android.app.Application
import android.content.Context
import com.blankj.utilcode.util.CrashUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import top.gangshanghua.xiaobo.simpleretrofit.simple.SimpleApi

class MainApplication : Application() {

    companion object {
        private lateinit var sAppContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        sAppContext = this

        initCrash()
        initLog()
        SimpleApi.init()
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