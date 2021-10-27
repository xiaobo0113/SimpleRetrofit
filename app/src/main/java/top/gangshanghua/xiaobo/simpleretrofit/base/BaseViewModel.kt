package top.gangshanghua.xiaobo.simpleretrofit.base

import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ParamViewModelFactory<VM : ViewModel>(
    private val factory: () -> VM,
    private val activity: BaseActivity
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return factory().apply {
            if (this is LoadingBaseViewModel && activity is LoadingBaseActivity) {
                mStartedTime = activity.mStartedTime
            }
        } as T
    }
}

inline fun <reified VM : ViewModel> BaseActivity.viewModel(
    noinline factory: () -> VM,
): Lazy<VM> = viewModels { ParamViewModelFactory(factory, this) }

open class BaseViewModel : ViewModel()

open class LoadingBaseViewModel : BaseViewModel() {
    lateinit var mStartedTime: String
}