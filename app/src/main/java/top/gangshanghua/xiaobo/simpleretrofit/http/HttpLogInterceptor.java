package top.gangshanghua.xiaobo.simpleretrofit.http;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

/**
 * 官方提供的 HttpLoggingInterceptor 不能将一个完整的请求日志包在一起，
 * 当有多个请求同时进行的时候，容易串行。
 * <p>
 * 使用该类，结合 {@link LogUtils} 可将一个完成的请求日志在一起打印出来。
 * <p>
 * 未打印 HEADERS。
 * <p>
 * 示例：
 * ┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────
 * │ OkHttp http://xxx/..., com.xxx.MyHttpLogInterceptor.intercept(MyHttpLogInterceptor.java:109)
 * ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 * │ --> POST http://xxx/getXXX
 * │ {
 * │  "data": null,
 * │  "code": 200,
 * │  "message": "提交成功"
 * │ }
 * │ <-- 200
 * └────────────────────────────────────────────────────────────────────────────────────────────────────────────────
 */
public class HttpLogInterceptor implements Interceptor {

    private boolean mEnableBody;

    public HttpLogInterceptor() {
        this(false);
    }

    public HttpLogInterceptor(boolean enableBody) {
        this.mEnableBody = enableBody;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        // 打印请求方法和 url 地址
        StringBuilder sb = new StringBuilder("--> ");
        sb.append(request.method()).append(" ").append(request.url().toString());

        // 打印 RequestBody
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            String requestBodyStr = buffer.readString(StandardCharsets.UTF_8);
            if (!TextUtils.isEmpty(requestBodyStr)) {
                sb.append(" ").append(requestBodyStr);
            }
        }
        sb.append("\n");

        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            sb.append("<-- HTTP FAILED: ").append(e.toString());
            LogUtils.d(sb.toString());
            throw e;
        }

        // 打印 ResponseBody
        if (mEnableBody) {
            ResponseBody responseBody = response.body();
            if (null != responseBody) {
                Headers headers = response.headers();
                BufferedSource source = responseBody.source();
                // Buffer the entire body.
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.getBuffer();

                if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
                    GzipSource gzipSource = new GzipSource(buffer.clone());
                    buffer = new Buffer();
                    buffer.writeAll(gzipSource);
                    gzipSource.close();
                }

                String body = buffer.clone().readString(StandardCharsets.UTF_8);
                if (!TextUtils.isEmpty(body)) {
                    String formatted = JsonUtils.formatJson(body, 1);
                    if (formatted.length() < 700) {
                        sb.append(formatted);
                    } else {
                        sb.append(body);
                    }
                } else {
                    sb.append("no body.");
                }
                sb.append("\n");
            }
        }

        // 打印响应码
        sb.append("<-- ").append(response.code()).append(" ").append(response.message());
        LogUtils.d(sb.toString());

        return response;
    }

}
