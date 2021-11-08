package top.gangshanghua.xiaobo.simpleretrofit

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {

    private lateinit var mLinearLayout: LinearLayout
    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLinearLayout = LinearLayout(this)
        mLinearLayout.orientation = LinearLayout.VERTICAL
        setContentView(ScrollView(this).apply {
            addView(mLinearLayout)
        })

        add(SimpleRetrofitActivity::class)
    }

    private fun add(kClass: KClass<out Activity>) {
        mHandler.removeCallbacksAndMessages(null)

        Button(this).apply {
            text = kClass.simpleName
            isAllCaps = false
            setOnClickListener {
                startActivity(Intent().apply {
                    component = ComponentName(packageName, kClass.qualifiedName!!)
                })
            }

            // 自动打开最后一个添加的 Activity, 方便测试
            mHandler.postDelayed(500L) {
                performClick()
            }
            mLinearLayout.addView(this, 0)
        }
    }

}