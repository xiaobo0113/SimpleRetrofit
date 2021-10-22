package top.gangshanghua.xiaobo.simpleretrofit.base

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ParamViewModelFactory<VM : ViewModel>(
    private val factory: () -> VM,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = factory() as T
}

inline fun <reified VM : ViewModel> AppCompatActivity.viewModel(
    noinline factory: () -> VM,
): Lazy<VM> = viewModels { ParamViewModelFactory(factory) }

open class BaseViewModel(val mStartedTime: String) : ViewModel()