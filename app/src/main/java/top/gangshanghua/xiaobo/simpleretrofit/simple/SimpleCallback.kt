package top.gangshanghua.xiaobo.simpleretrofit.simple

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO add IgnoreResponseSimpleCallback
class IgnoreResponseSimpleCallback

class SimpleCallback<T>(private val mLiveData: MutableLiveData<T?>) : Callback<Result<T?>> {

    override fun onResponse(call: Call<Result<T?>>, response: Response<Result<T?>>) {
        if (response.isSuccessful) {
            // when isSuccessful is true, body() must not be null
            val result = response.body()!!
            if (result.code != 200 && !result.message.isNullOrEmpty()) {
                ToastUtils.showShort(result.message)
            }

            mLiveData.value = result.data
        } else {
            // fail: such as 404/500, show tips
            ToastUtils.showShort(response.message())
            mLiveData.value = null
        }
    }

    override fun onFailure(call: Call<Result<T?>>, t: Throwable) {
        mLiveData.value = null
        // already use global exception tips, no use here any more
        // ToastUtils.showShort(t.message)
    }

}