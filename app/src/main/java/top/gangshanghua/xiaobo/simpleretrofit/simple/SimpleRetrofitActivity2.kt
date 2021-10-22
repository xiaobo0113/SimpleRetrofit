package top.gangshanghua.xiaobo.simpleretrofit.simple

import android.os.Bundle
import top.gangshanghua.xiaobo.simpleretrofit.base.LoadingBaseActivity

class SimpleRetrofitActivity2 : LoadingBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SimpleApi.mApiService.good(mStartedTime).enqueue(IgnoreResponseSimpleCallback())
    }

    override fun forbidBackPressWhenLoading() = false

}