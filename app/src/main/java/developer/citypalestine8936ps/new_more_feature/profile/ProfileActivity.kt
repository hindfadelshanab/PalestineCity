package developer.citypalestine8936ps.new_more_feature.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.SignInActivity
import developer.citypalestine8936ps.databinding.ActivityProfileBinding
import developer.citypalestine8936ps.models.User
import developer.citypalestine8936ps.utilites.Constants
import developer.citypalestine8936ps.utilites.PreferenceManager
import developer.citypalestine8936ps.utilites.load


class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityProfileBinding

    private lateinit var database: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var user: User
    private var loggedUserId: String = ""

    private lateinit var preferenceManager: PreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            cvUserChangeName.setOnClickListener(this@ProfileActivity)
            cvUserChangePassword.setOnClickListener(this@ProfileActivity)
            cvUserLogout.setOnClickListener(this@ProfileActivity)
        }

        preferenceManager = PreferenceManager(this)
        database = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        supportActionBar?.hide()

        loggedUserId = PreferenceManager(this).getString(Constants.KEY_USER_ID)
        initLoggedUserData()
    }


    private fun initLoggedUserData() {
        database.collection(Constants.KEY_COLLECTION_USERS)
            .document(loggedUserId)
            .addSnapshotListener { doc, _ ->
                user = doc?.toObject(User::class.java) ?: return@addSnapshotListener
                user.let {
                    binding.txtUserNameProfile.text = user.name.toString()
                    binding.txtUserEmailProfile.text = user.email.toString()
                    binding.tvUserLocation.text = user.city.toString()

                    binding.profileImage.load(this, user.image)
                }
            }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.cvUserChangeName -> onClickChangeName()
            binding.cvUserChangePassword -> onClickChangePassword()
            binding.cvUserLogout -> onClickLogout()
        }
    }

    private fun onClickChangeName() {
        NameBottomSheetFragment.display(supportFragmentManager, user.name)
    }

    private fun onClickChangePassword() {
        Toast.makeText(this, getString(R.string.not_ready), Toast.LENGTH_SHORT).show()
    }

    private fun onClickLogout() {
        Toast.makeText(this, getString(R.string.logging_out), Toast.LENGTH_LONG).show();
        preferenceManager.clear()
        firebaseAuth.signOut()
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

}