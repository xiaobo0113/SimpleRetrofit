package top.gangshanghua.xiaobo.simpleretrofit.http

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import top.gangshanghua.xiaobo.lib_simple.http.Result
import top.gangshanghua.xiaobo.lib_simple.SimpleRetrofit
import top.gangshanghua.xiaobo.simpleretrofit.SimpleRetrofitActivity

interface ApiService {

    @GET("good")
    fun good(): Call<Result<List<SimpleRetrofitActivity.Item>?>>

    @GET("notFound")
    fun notFound(): Call<Result<List<SimpleRetrofitActivity.Item>?>>

    @GET("timeout")
    fun timeout(): Call<Result<List<SimpleRetrofitActivity.Item>?>>

}