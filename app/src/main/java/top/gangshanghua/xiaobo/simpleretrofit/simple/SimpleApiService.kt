package top.gangshanghua.xiaobo.simpleretrofit.simple

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface SimpleApiService {

    // add header {"loading", "true"} manually
    @GET("good")
    fun good(@Header(SimpleApi.HEADER_LOADING) showLoading: Boolean = false):
            Call<Result<List<SimpleRetrofitActivity.Item>?>>

    @GET("notFound")
    fun notFound(): Call<Result<List<SimpleRetrofitActivity.Item>?>>

    @GET("timeout")
    fun timeout(): Call<Result<List<SimpleRetrofitActivity.Item>?>>

}

/**
 * add header {"loading", "true"} automatically for you
 */
interface SimpleLoadingApiService {

    @GET("good")
    fun good(): Call<Result<List<SimpleRetrofitActivity.Item>?>>

    @GET("notFound")
    fun notFound(): Call<Result<List<SimpleRetrofitActivity.Item>?>>

    @GET("timeout")
    fun timeout(): Call<Result<List<SimpleRetrofitActivity.Item>?>>

}