package top.gangshanghua.xiaobo.simpleretrofit.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment

class SimpleLoadingDialog : DialogFragment() {

    companion object {
        fun newInstance(): SimpleLoadingDialog {
            val dialog = SimpleLoadingDialog()
            dialog.arguments = Bundle().apply {
                // add arguments
            }
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ProgressBar(requireContext())
    }

}