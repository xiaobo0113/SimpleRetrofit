package top.gangshanghua.xiaobo.lib_simple.loading

import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

abstract class LoadingActivity : AppCompatActivity(), Loading {
    override var mLoading: Int = 0
    override val mHandler: Handler = Handler(Looper.getMainLooper())
}