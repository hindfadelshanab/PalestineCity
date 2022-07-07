package developer.citypalestine8936ps.new_more_feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.databinding.FragmentPasswordBottomSheetBinding
import developer.citypalestine8936ps.utilites.Constants
import developer.citypalestine8936ps.utilites.PreferenceManager


class PasswordBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: FragmentPasswordBottomSheetBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    private var currentUser: FirebaseUser? = null
    private var loggedUserId: String = ""
    private var loggedUserEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        currentUser = auth.currentUser

        loggedUserId = PreferenceManager(requireContext()).getString(Constants.KEY_USER_ID)
        arguments?.let {
            loggedUserEmail = it.getString(Constants.KEY_USER_EMAIL, "") ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPasswordBottomSheetBinding.inflate(inflater, container, false)
        return binding.apply {
            btnSave.setOnClickListener(this@PasswordBottomSheetFragment)
            ibClose.setOnClickListener(this@PasswordBottomSheetFragment)
        }.root
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
        val oldPassword = binding.etOldPassword.text?.toString() ?: ""
        val newPassword = binding.etNewPassword.text?.toString() ?: ""
        val newConfirmPassword = binding.etConfirmNewPassword.text?.toString() ?: ""
        if (oldPassword.isEmpty()) {
            binding.etOldPassword.error = getString(R.string.error_field_empty)
        } else if (newPassword.isEmpty()) {
            binding.etNewPassword.error = getString(R.string.error_field_empty)
        } else if (newConfirmPassword.isEmpty()) {
            binding.etConfirmNewPassword.error = getString(R.string.error_field_empty)
        } else if (newPassword != newConfirmPassword) {
            binding.etNewPassword.error = getString(R.string.error_passwords_not_match)
            binding.etConfirmNewPassword.error = getString(R.string.error_passwords_not_match)
        } else {
            val credential = EmailAuthProvider.getCredential(loggedUserEmail, oldPassword)
            currentUser?.reauthenticate(credential)?.addOnCompleteListener { reAuthResult ->
                if (!reAuthResult.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.somthing_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addOnCompleteListener
                }
                currentUser?.updatePassword(newPassword)
                    ?.addOnCompleteListener { updatePassResult ->
                        if (!updatePassResult.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Password dose not changed",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Password Changed successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            dismiss()
                        }
                    }
            } ?: kotlin.run {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.somthing_wrong),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val TAG = "PasswordBottomSheetFrag"


        fun display(
            fragmentManager: FragmentManager,
            currentEmail: String,
        ) {
            val dialog = PasswordBottomSheetFragment()
            dialog.arguments = bundleOf(Constants.KEY_USER_EMAIL to currentEmail)
            dialog.show(fragmentManager, TAG)
        }
    }
}