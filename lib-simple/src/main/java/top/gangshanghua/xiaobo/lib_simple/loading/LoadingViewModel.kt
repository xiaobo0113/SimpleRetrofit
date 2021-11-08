package top.gangshanghua.xiaobo.lib_simple.loading

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ParamViewModelFactory<VM : ViewModel>(
    private val factory: () -> VM,
    private val loading: Loading
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return factory().apply {
            if (this is LoadingViewModel) {
                mUUID = loading.getUUID()
            }
        } as T
    }
}

inline fun <reified VM : ViewModel> Loading.viewModel(
    noinline factory: () -> VM,
): Lazy<VM> = (this as AppCompatActivity).viewModels {
    ParamViewModelFactory(factory, this)
}

open class LoadingViewModel : ViewModel() {
    lateinit var mUUID: String
}