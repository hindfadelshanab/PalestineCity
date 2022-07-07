package developer.citypalestine8936ps.new_more_feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import developer.citypalestine8936ps.databinding.FragmentNameBottomSheetBinding
import developer.citypalestine8936ps.utilites.Constants
import developer.citypalestine8936ps.utilites.PreferenceManager

class NameBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: FragmentNameBottomSheetBinding
    private lateinit var database: FirebaseFirestore

    private var currentValue: String = ""
    private var loggedUserId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNameBottomSheetBinding.inflate(inflater, container, false)
        return binding.apply {
            btnSave.setOnClickListener(this@NameBottomSheetFragment)
            ibClose.setOnClickListener(this@NameBottomSheetFragment)
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseFirestore.getInstance()
        loggedUserId = PreferenceManager(requireContext()).getString(Constants.KEY_USER_ID)
        arguments?.let {
            currentValue = it.getString(Constants.KEY_CURRENT_NAME, "") ?: ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etUserEditField.setText(currentValue)

    }


    override fun onClick(v: View?) {
        when (v ?: return) {
            binding.btnSave -> onClickSave()
            binding.ibClose -> onClickClose()
        }
    }

    private fun onClickClose() {
        dismiss()
    }

    private fun onClickSave() {
        val newValue = binding.etUserEditField.text?.toString()
        newValue?.let {
            database
                .collection(Constants.KEY_COLLECTION_USERS)
                .document(loggedUserId)
                .update("name", newValue)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Update failed!!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }

    }

    companion object {
        private const val TAG = "EditTextBottomSheetFrag"

        fun display(
            fragmentManager: FragmentManager,
            currentName: String,
//            onUpdateSuccess: () -> Unit
        ) {
            val dialog = NameBottomSheetFragment()
            dialog.arguments = bundleOf(Constants.KEY_CURRENT_NAME to currentName)
//            dialog.arguments = bundleOf(Constants.KEY_ON_SUCCESS to onUpdateSuccess)
            dialog.show(fragmentManager, TAG)
        }
    }
}