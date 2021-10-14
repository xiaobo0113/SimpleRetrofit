package top.gangshanghua.xiaobo.simpleretrofit.simple

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface SimpleApiService {

    @GET("good")
    fun good(@Header(SimpleApi.HEADER_LOADING) loading: Boolean = false):
            Call<Result<List<SimpleRetrofitActivity.Item>?>>

    @GET("notFound")
    fun notFound(@Header(SimpleApi.HEADER_LOADING) loading: Boolean = false):
            Call<Result<List<SimpleRetrofitActivity.Item>?>>

    @GET("timeout")
    fun timeout(@Header(SimpleApi.HEADER_LOADING) loading: Boolean = false):
            Call<Result<List<SimpleRetrofitActivity.Item>?>>

}