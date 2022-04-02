package top.gangshanghua.xiaobo.simpleretrofit.http

import android.content.Context
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import top.gangshanghua.xiaobo.lib_simple.SimpleRetrofit
import java.util.concurrent.TimeUnit

object SimpleApi {

    lateinit var mApiService: ApiService
    lateinit var mLoadingApiService: LoadingApiService

    fun init(context: Context) {
        val client = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            // .callTimeout(3, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool())
            .addInterceptor(HttpLogInterceptor(true))
            .addInterceptor(MockInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://www.baidu.com/")
            .addConverterFactory(GsonConverterFactoryWithGlobalCheck.create())
            .build()

        mApiService = retrofit.create(ApiService::class.java)
        mLoadingApiService = retrofit.newBuilder()
            .client(SimpleRetrofit.init(context, client.newBuilder()).build())
            .build()
            .create(LoadingApiService::class.java)
    }

}