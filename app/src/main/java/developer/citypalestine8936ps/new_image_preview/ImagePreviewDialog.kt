package developer.citypalestine8936ps.new_image_preview

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.databinding.DialogImagePreviewBinding
import developer.citypalestine8936ps.utilites.Constants
import developer.citypalestine8936ps.utilites.load
import developer.citypalestine8936ps.utilites.loadImageUrl

class ImagePreviewDialog : DialogFragment() {
    private lateinit var binding: DialogImagePreviewBinding
    private var imageUrl: String = ""

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)

        arguments?.let {
            imageUrl = it.getString(Constants.KEY_IMAGE_PREVIEW_URL, "") ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogImagePreviewBinding.inflate(inflater)
        return binding.apply {
            ibBack.setOnClickListener { dismiss() }
//            toolbar.setNavigationOnClickListener { dismiss() }
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: imageUrl $imageUrl")
        binding.ivImagePreview.load(requireContext(), imageUrl)
    }


    companion object {
        private const val TAG = "ImagePreviewDialog"
        fun display(
            fragmentManager: FragmentManager,
            imageUrl: String = ""
        ): ImagePreviewDialog {
            val commentsDialog = ImagePreviewDialog()
            imageUrl.let {
                commentsDialog.arguments = bundleOf(Constants.KEY_IMAGE_PREVIEW_URL to imageUrl)
            }
            commentsDialog.show(fragmentManager, TAG)
            return commentsDialog
        }
    }

}
