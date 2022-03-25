package top.gangshanghua.xiaobo.lib_simple.http

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IgnoreResponseSimpleCallback<T> : SimpleCallback<T>(MutableLiveData<T?>())

open class SimpleCallback<T>(private val mLiveData: MutableLiveData<T?>) : Callback<Result<T?>> {

    override fun onResponse(call: Call<Result<T?>>, response: Response<Result<T?>>) {
        val result = response.body()
        if (result != null) {
            if (result.code != 200 && !result.message.isNullOrEmpty()) {
                ToastUtils.showShort(result.message)
            }

            mLiveData.value = result.data
        } else {
            // eg: such as 404/500 or 204/205
            ToastUtils.showShort(response.message())
            mLiveData.value = null
        }
    }

    override fun onFailure(call: Call<Result<T?>>, t: Throwable) {
        mLiveData.value = null
        // already use global exception tips, no use here any more
        ToastUtils.showShort(t.message)
    }

}