package top.gangshanghua.xiaobo.simpleretrofit.simple

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import top.gangshanghua.xiaobo.simpleretrofit.http.Result

interface SimpleApiService {

    @GET("good")
    fun good(@Header(SimpleApi.HEADER_UUID) targetActivityUUID: String = ""):
            Call<Result<List<SimpleRetrofitActivity.Item>?>>

    @GET("notFound")
    fun notFound(@Header(SimpleApi.HEADER_UUID) targetActivityUUID: String = ""):
            Call<Result<List<SimpleRetrofitActivity.Item>?>>

    @GET("timeout")
    fun timeout(@Header(SimpleApi.HEADER_UUID) targetActivityUUID: String = ""):
            Call<Result<List<SimpleRetrofitActivity.Item>?>>

}