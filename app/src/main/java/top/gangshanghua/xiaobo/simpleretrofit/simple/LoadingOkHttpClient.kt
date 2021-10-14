package top.gangshanghua.xiaobo.simpleretrofit.simple

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request

class LoadingOkHttpClient(private val client: OkHttpClient, private val enable: Boolean) : OkHttpClient() {

    override fun newCall(request: Request): Call {
        return if (enable) {
            client.newCall(
                request.newBuilder().addHeader(SimpleApi.HEADER_LOADING, "true").build()
            )
        } else {
            client.newCall(request)
        }
    }

}