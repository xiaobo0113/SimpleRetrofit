package top.gangshanghua.xiaobo.lib_simple.loading

import androidx.appcompat.app.AppCompatActivity

abstract class LoadingActivity : AppCompatActivity(), Loading {
    override var mLoading: Int = 0
}