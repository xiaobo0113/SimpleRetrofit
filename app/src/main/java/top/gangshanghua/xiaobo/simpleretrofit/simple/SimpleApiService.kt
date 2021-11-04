package top.gangshanghua.xiaobo.simpleretrofit.simple

import retrofit2.Call
import retrofit2.http.GET
import top.gangshanghua.xiaobo.lib_annotation.UUIDHeader
import top.gangshanghua.xiaobo.simpleretrofit.http.Result

interface SimpleApiService {

    @GET("good")
    fun good(@UUIDHeader uuid: String = ""): Call<Result<List<SimpleRetrofitActivity.Item>?>>

    @GET("notFound")
    fun notFound(@UUIDHeader uuid: String = ""): Call<Result<List<SimpleRetrofitActivity.Item>?>>

    @GET("timeout")
    fun timeout(@UUIDHeader uuid: String = ""): Call<Result<List<SimpleRetrofitActivity.Item>?>>

}