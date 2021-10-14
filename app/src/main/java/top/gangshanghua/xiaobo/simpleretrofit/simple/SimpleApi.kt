package top.gangshanghua.xiaobo.simpleretrofit.simple

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import okhttp3.Call
import okhttp3.ConnectionPool
import okhttp3.EventListener
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object SimpleApi {

    const val HEADER_LOADING = "loading"

    lateinit var mSimpleApiService: SimpleApiService
    val mLoadingLiveData = MutableLiveData<Pair<Boolean, Call>>()

    fun init() {
        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .connectTimeout(3, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool())
            .addInterceptor(SimpleInterceptor())
            .eventListener(object : EventListener() {
                override fun callStart(call: Call) {
                    super.callStart(call)
                    if ("true" == call.request().header(HEADER_LOADING)) {
                        mLoadingLiveData.postValue(Pair(true, call))
                    }
                }

                override fun callEnd(call: Call) {
                    super.callEnd(call)
                    if ("true" == call.request().header(HEADER_LOADING)) {
                        mLoadingLiveData.postValue(Pair(false, call))
                    }
                }

                override fun callFailed(call: Call, ioe: IOException) {
                    super.callFailed(call, ioe)
                    if ("true" == call.request().header(HEADER_LOADING)) {
                        mLoadingLiveData.postValue(Pair(false, call))
                    }

                    LogUtils.d("call of " + call.request().url() + " failed. " + ioe)
                    ToastUtils.showShort("Network error, Please retry later.")
                }
            })
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://www.baidu.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mSimpleApiService = retrofit.create(SimpleApiService::class.java)
    }

}