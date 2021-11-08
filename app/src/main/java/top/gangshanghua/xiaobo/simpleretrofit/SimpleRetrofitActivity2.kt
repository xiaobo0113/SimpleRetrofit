package top.gangshanghua.xiaobo.simpleretrofit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.blankj.utilcode.util.ToastUtils
import top.gangshanghua.xiaobo.lib_simple.http.IgnoreResponseSimpleCallback
import top.gangshanghua.xiaobo.simpleretrofit.base.BaseActivity
import top.gangshanghua.xiaobo.simpleretrofit.http.SimpleApi

class SimpleRetrofitActivity2 : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SimpleApi.mApiService.good(getUUID()).enqueue(IgnoreResponseSimpleCallback())

        if (null == savedInstanceState) {
            MyDialog().show(supportFragmentManager, "MyDialog")
        }
    }

    class MyDialog : DialogFragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return TextView(requireContext()).apply {
                text = "MyDialog"
                setOnClickListener {
                    ToastUtils.showShort("MyDialog")
                }
            }
        }
    }

}