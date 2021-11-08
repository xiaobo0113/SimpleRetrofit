package top.gangshanghua.xiaobo.simpleretrofit.http

import android.content.Context
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import top.gangshanghua.xiaobo.lib_simple.SimpleRetrofit
import java.util.concurrent.TimeUnit

object SimpleApi {

    lateinit var mApiService: SimpleApiService

    fun init(context: Context) {
        val builder = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            // .callTimeout(3, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool())
            .addInterceptor(HttpLogInterceptor(true))
            .addInterceptor(MockInterceptor())

        val retrofit = Retrofit.Builder()
            .client(SimpleRetrofit.init(context, builder))
            .baseUrl("https://www.baidu.com/")
            .addConverterFactory(GsonConverterFactoryWithGlobalCheck.create())
            .build()

        mApiService = retrofit.create(SimpleApiService::class.java)
    }

}