package top.gangshanghua.xiaobo.simpleretrofit.http

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import top.gangshanghua.xiaobo.lib_simple.http.Result
import top.gangshanghua.xiaobo.lib_simple.SimpleRetrofit
import top.gangshanghua.xiaobo.simpleretrofit.SimpleRetrofitActivity

interface SimpleApiService {

    @GET("good")
    fun good(@Header(SimpleRetrofit.HEADER_UUID) uuid: String = ""):
            Call<Result<List<SimpleRetrofitActivity.Item>?>>

    @GET("notFound")
    fun notFound(@Header(SimpleRetrofit.HEADER_UUID) uuid: String = ""):
            Call<Result<List<SimpleRetrofitActivity.Item>?>>

    @GET("timeout")
    fun timeout(@Header(SimpleRetrofit.HEADER_UUID) uuid: String = ""):
            Call<Result<List<SimpleRetrofitActivity.Item>?>>

}