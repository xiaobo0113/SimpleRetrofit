package top.gangshanghua.xiaobo.simpleretrofit.simple

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import top.gangshanghua.xiaobo.simpleretrofit.LoadingBaseActivity
import top.gangshanghua.xiaobo.simpleretrofit.R

class SimpleRetrofitActivity : LoadingBaseActivity() {

    // =============================================================================>
    data class Item(val name: String?)

    class MyViewModel : ViewModel() {
        // do not use one same LiveData for multiple requests.
        // for only the last result will be send.
        val mTestData = MutableLiveData<List<Item>?>()

        fun good(showLoading: Boolean = false) {
            SimpleApi.mSimpleApiService.good(showLoading).enqueue(SimpleCallback(mTestData))
        }

        fun notFound(showLoading: Boolean = false) {
            SimpleApi.mSimpleApiService.notFound(showLoading).enqueue(SimpleCallback(mTestData))
        }

        fun timeout(showLoading: Boolean = false) {
            SimpleApi.mSimpleApiService.timeout(showLoading).enqueue(SimpleCallback(mTestData))
        }
    }

    class MyAdapter :
        BaseQuickAdapter<Item, BaseViewHolder>(android.R.layout.simple_list_item_1, null) {
        override fun convert(holder: BaseViewHolder, item: Item) {
            holder.setText(android.R.id.text1, item.name)
        }
    }
    // <=============================================================================

    private val mViewModel: MyViewModel by viewModels()
    private lateinit var mAdapter: MyAdapter
    private var mShowLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_retrofit)

        initUi()
        initViewModel()
    }

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
            mViewModel.good(mShowLoading)
        }

        findViewById<Button>(R.id.not_found).setOnClickListener {
            mViewModel.notFound(mShowLoading)
        }

        findViewById<Button>(R.id.timeout).setOnClickListener {
            mViewModel.timeout(mShowLoading)
        }

        findViewById<Button>(R.id.multiple).setOnClickListener {
            mViewModel.notFound(mShowLoading)
            mViewModel.timeout(mShowLoading)
        }

        findViewById<CheckBox>(R.id.show_loading).setOnCheckedChangeListener { _, isChecked ->
            mShowLoading = isChecked
        }
    }

}