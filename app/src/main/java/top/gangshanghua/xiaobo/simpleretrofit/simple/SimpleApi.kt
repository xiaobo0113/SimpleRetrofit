package top.gangshanghua.xiaobo.simpleretrofit.simple

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import okhttp3.Call
import okhttp3.ConnectionPool
import okhttp3.EventListener
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import top.gangshanghua.xiaobo.simpleretrofit.HttpLogInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

object SimpleApi {

    const val HEADER_LOADING = "loading"

    lateinit var mApiService: SimpleApiService
    lateinit var mLoadingApiService: SimpleLoadingApiService

    private val mHandler = Handler(Looper.getMainLooper())

    val mLoadingLiveData = MutableLiveData<Pair<Boolean, Call>>()

    fun init() {
        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .connectTimeout(3, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool())
            .addInterceptor(HttpLogInterceptor(true))
            .addInterceptor(SimpleInterceptor())
            .eventListener(object : EventListener() {
                override fun callStart(call: Call) {
                    super.callStart(call)
                    // can not use postValue, for maybe only the last value will be sent
                    if ("true" == call.request().header(HEADER_LOADING)) {
                        mHandler.post {
                            mLoadingLiveData.value = Pair(true, call)
                        }
                    }
                }

                override fun callEnd(call: Call) {
                    super.callEnd(call)
                    if ("true" == call.request().header(HEADER_LOADING)) {
                        mHandler.post {
                            mLoadingLiveData.value = Pair(false, call)
                        }
                    }
                }

                override fun callFailed(call: Call, ioe: IOException) {
                    super.callFailed(call, ioe)
                    if ("true" == call.request().header(HEADER_LOADING)) {
                        mHandler.post {
                            mLoadingLiveData.value = Pair(false, call)
                        }
                    }

                    // already used global log interceptor.
                    // LogUtils.d("call of " + call.request().url() + " failed. " + ioe)
                    ToastUtils.showShort("Network error, Please retry later.")
                }
            })
            .build()

        val retrofit = Retrofit.Builder()
            .client(LoadingOkHttpClient(client, false))
            .baseUrl("https://www.baidu.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mApiService = retrofit.create(SimpleApiService::class.java)
        mLoadingApiService = retrofit.newBuilder()
            .client(LoadingOkHttpClient(client, true))
            .build()
            .create(SimpleLoadingApiService::class.java)
    }

}