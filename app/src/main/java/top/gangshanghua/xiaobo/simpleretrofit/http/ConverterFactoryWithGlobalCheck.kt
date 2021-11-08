package top.gangshanghua.xiaobo.simpleretrofit.http

import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import top.gangshanghua.xiaobo.lib_simple.http.Result
import java.io.IOException
import java.lang.reflect.Type

class GsonConverterFactoryWithGlobalCheck private constructor() : Converter.Factory() {

    companion object {
        fun create() = GsonConverterFactoryWithGlobalCheck()
    }

    private val mGson: Gson = Gson()
    private val mOriginalFactory: GsonConverterFactory = GsonConverterFactory.create(mGson)

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val adapter: TypeAdapter<*> = mGson.getAdapter(TypeToken.get(type))
        return GsonResponseBodyConverterWithGlobalCheck(mGson, adapter)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return mOriginalFactory.requestBodyConverter(
            type,
            parameterAnnotations,
            methodAnnotations,
            retrofit
        )
    }

}

class GsonResponseBodyConverterWithGlobalCheck<T>(
    private val gson: Gson,
    private val adapter: TypeAdapter<T>
) : Converter<ResponseBody, T> {
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val jsonReader = gson.newJsonReader(value.charStream())
        return value.use { _ ->
            val result = adapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed.")
            }

            if (result is Result<*>) {
                LogUtils.d("GlobalCheck, code: ${result.code}")

                when (result.code) {
                    // eg: need login, jump to LoginActivity
                    10001 -> {
                    }

                    // eg: server in maintenance, show Dialog
                    10002 -> {
                    }
                }
            }

            result
        }
    }
}