package top.gangshanghua.xiaobo.simpleretrofit.simple

import okhttp3.*
import java.net.SocketTimeoutException

class SimpleInterceptor : Interceptor {

    companion object {
        private const val GOOD_JSON = """
                {
                    "code": 200,
                    "data": [
                        {"name": "this is 0"},
                        {"name": "this is 1"},
                        {"name": "this is 2"},
                        {"name": "this is 3"},
                        {"name": "this is 4"}
                    ],
                    "message": "success"
                }
            """
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        when (chain.request().url().pathSegments().last()) {
            "good" -> {
                Thread.sleep(1000L)

                val body = ResponseBody.create(MediaType.parse("application/json"), GOOD_JSON)
                return Response.Builder()
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .body(body)
                    .message("OK")
                    .build()
            }

            "timeout" -> {
                Thread.sleep(1500L)
                throw SocketTimeoutException("timeout")
            }

            else -> {
                Thread.sleep(2000L)
                return chain.proceed(chain.request())
            }
        }
    }

}