package top.gangshanghua.xiaobo.simpleretrofit.simple

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import top.gangshanghua.xiaobo.simpleretrofit.LoadingBaseActivity

class SimpleRetrofitActivity2 : LoadingBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SimpleApi.mApiService.good(mStartedTime).enqueue(SimpleCallback(MutableLiveData()))
    }

    override fun forbidBackPressWhenLoading() = false

}