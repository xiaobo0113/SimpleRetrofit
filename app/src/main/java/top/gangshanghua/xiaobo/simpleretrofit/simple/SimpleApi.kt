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
import top.gangshanghua.xiaobo.simpleretrofit.http.GsonConverterFactoryWithGlobalCheck
import top.gangshanghua.xiaobo.simpleretrofit.http.HttpLogInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

object SimpleApi {

    const val HEADER_UUID = "loading"

    lateinit var mApiService: SimpleApiService
    private val mHandler = Handler(Looper.getMainLooper())
    val mLoadingLiveData = MutableLiveData<Triple<Boolean, String, Call>>()

    fun init() {
        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .connectTimeout(3, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool())
            .addInterceptor(
                HttpLogInterceptor(
                    true
                )
            )
            .addInterceptor(SimpleInterceptor())
            .eventListener(object : EventListener() {
                override fun callStart(call: Call) {
                    super.callStart(call)
                    // can not use postValue, for maybe only the last value will be sent
                    call.request().header(HEADER_UUID)?.let {
                        mHandler.post {
                            mLoadingLiveData.value = Triple(true, it, call)
                        }
                    }
                }

                override fun callEnd(call: Call) {
                    super.callEnd(call)
                    call.request().header(HEADER_UUID)?.let {
                        mHandler.post {
                            mLoadingLiveData.value = Triple(false, it, call)
                        }
                    }
                }

                override fun callFailed(call: Call, ioe: IOException) {
                    super.callFailed(call, ioe)
                    call.request().header(HEADER_UUID)?.let {
                        mHandler.post {
                            mLoadingLiveData.value = Triple(false, it, call)
                        }
                    }

                    // already used global log interceptor.
                    // LogUtils.d("call of " + call.request().url() + " failed. " + ioe)
                    ToastUtils.showShort("Network error, Please retry later.")
                }
            })
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://www.baidu.com/")
            .addConverterFactory(GsonConverterFactoryWithGlobalCheck.create())
            .build()

        mApiService = retrofit.create(SimpleApiService::class.java)
    }

}