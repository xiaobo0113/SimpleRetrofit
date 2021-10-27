package top.gangshanghua.xiaobo.simpleretrofit.simple

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import top.gangshanghua.xiaobo.simpleretrofit.R
import top.gangshanghua.xiaobo.simpleretrofit.base.BaseViewModel
import top.gangshanghua.xiaobo.simpleretrofit.base.LoadingBaseActivity
import top.gangshanghua.xiaobo.simpleretrofit.base.LoadingBaseViewModel
import top.gangshanghua.xiaobo.simpleretrofit.base.viewModel

class SimpleRetrofitActivity : LoadingBaseActivity() {

    // =============================================================================>
    data class Item(val name: String?)

    class MyViewModel : LoadingBaseViewModel() {
        // do not use one same LiveData for multiple requests.
        // for only the last result will be send.
        val mTestData = MutableLiveData<List<Item>?>()

        fun good() {
            SimpleApi.mApiService.good().enqueue(SimpleCallback(mTestData))
        }

        fun goodLoading() {
            SimpleApi.mApiService.good(mStartedTime).enqueue(SimpleCallback(mTestData))
        }

        fun notFound() {
            SimpleApi.mApiService.notFound().enqueue(SimpleCallback(mTestData))
        }

        fun notFoundLoading() {
            SimpleApi.mApiService.notFound(mStartedTime).enqueue(SimpleCallback(mTestData))
        }

        fun timeout() {
            SimpleApi.mApiService.timeout().enqueue(SimpleCallback(mTestData))
        }

        fun timeoutLoading() {
            SimpleApi.mApiService.timeout(mStartedTime).enqueue(SimpleCallback(mTestData))
        }
    }

    class MyAdapter :
        BaseQuickAdapter<Item, BaseViewHolder>(android.R.layout.simple_list_item_1, null) {
        override fun convert(holder: BaseViewHolder, item: Item) {
            holder.setText(android.R.id.text1, item.name)
        }
    }
    // <=============================================================================

    private val mViewModel: MyViewModel by viewModel {
        MyViewModel()
    }
    private lateinit var mAdapter: MyAdapter
    private var mShowLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_retrofit)

        initUi()
        initViewModel()
    }

    override fun forbidBackPressWhenLoading() = true

    private fun initViewModel() {
        mViewModel.mTestData.observe(this) {
            it?.let {
                mAdapter.setList(it)
            }
        }
    }

    private fun initUi() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter().apply {
            mAdapter = this
        }
        mAdapter.setEmptyView(TextView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            gravity = Gravity.CENTER
            text = "Empty!"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        })

        findViewById<Button>(R.id.good).setOnClickListener {
            if (!mShowLoading) {
                mViewModel.good()
            } else {
                mViewModel.goodLoading()
            }
        }

        findViewById<Button>(R.id.not_found).setOnClickListener {
            if (!mShowLoading) {
                mViewModel.notFound()
            } else {
                mViewModel.notFoundLoading()
            }
        }

        findViewById<Button>(R.id.timeout).setOnClickListener {
            if (!mShowLoading) {
                mViewModel.timeout()
            } else {
                mViewModel.timeoutLoading()
            }
        }

        findViewById<Button>(R.id.multiple).setOnClickListener {
            if (!mShowLoading) {
                mViewModel.notFound()
                mViewModel.timeout()
            } else {
                mViewModel.notFoundLoading()
                mViewModel.timeoutLoading()
            }
        }

        findViewById<CheckBox>(R.id.show_loading).setOnCheckedChangeListener { _, isChecked ->
            mShowLoading = isChecked
        }

        findViewById<Button>(R.id.jump).setOnClickListener {
            startActivity(Intent(it.context, SimpleRetrofitActivity2::class.java))
        }
    }

}