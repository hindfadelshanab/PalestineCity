package developer.citypalestine8936ps

import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.*
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import developer.citypalestine8936ps.databinding.ActivityProfileBinding
import developer.citypalestine8936ps.databinding.ChangeNameDilaogBinding
import developer.citypalestine8936ps.models.User
import developer.citypalestine8936ps.utilites.Constants
import developer.citypalestine8936ps.utilites.PreferenceManager
import java.util.*


class ProfileActivity : AppCompatActivity() {

    private lateinit var database: FirebaseFirestore
    lateinit var binding: ActivityProfileBinding
    lateinit var firebaseAuth: FirebaseAuth


    // private lateinit var mMap: GoogleMap
    lateinit var preferenceManager: PreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)
        database = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        getSupportActionBar()?.setTitle("الملف الشخصي");


        getUserInfo(preferenceManager.getString(Constants.KEY_USER_ID))
        Picasso.get().load(preferenceManager.getString(Constants.KEY_IMAGE))
            .into(binding.profileImage)

        binding.txtSignOut.setOnClickListener {
            Toast.makeText(this, "signing out ....", Toast.LENGTH_LONG).show();
            val database = FirebaseFirestore.getInstance()
            val documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
                preferenceManager.getString(Constants.KEY_USER_ID)
            )
            val updates = HashMap<String, Any>()
            updates[Constants.KEY_FCM_TOKEN] = FieldValue.delete()
            documentReference.update(updates).addOnSuccessListener { unsend: Void? ->
                preferenceManager.clear()
                startActivity(Intent(this, SignInActivity::class.java))
                // finish()
            }.addOnFailureListener { e: Exception? ->
                Toast.makeText(this, "unable to sign out", Toast.LENGTH_LONG).show();
            }
        }

        binding.cardChangeName.setOnClickListener {
            showChangeNameDialog()
        }
        binding.cardChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        val userId = intent.getStringExtra("userId")
        if (userId !=null){
            binding.cardChangeName.visibility=View.GONE
            binding.cardChangePassword.visibility=View.GONE
            binding.txtSignOut.visibility=View.GONE
            getUserInfo(userId)
        }

        getSupportActionBar()?.setTitle("Profile");

    }


    fun getUserInfo(userid: String) {
        database.collection(Constants.KEY_COLLECTION_USERS).document(userid)
            .get().addOnSuccessListener { doc ->
                var user = doc.toObject(User::class.java)
                binding.txtUserNameProfile.text = user?.name.toString()
                binding.txtUserEmailProfile.text = user!!.email.toString()
                binding.txtUserCityProfile.text = user!!.city


            }


    }

    private fun showChangeNameDialog() {

        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val dialog = Dialog(this)

        dialog.setCancelable(true)

        val changeNameDilaogBinding: ChangeNameDilaogBinding =
            ChangeNameDilaogBinding.inflate(layoutInflater)

        dialog.setContentView(changeNameDilaogBinding.root)
        dialog.window!!.setLayout(6 * width / 7, 1 * height / 4)

        changeNameDilaogBinding.btnDone.setOnClickListener {

            val newName = changeNameDilaogBinding.inputNewValue.text.toString()
            if (!newName.isEmpty()) {

                database.collection(Constants.KEY_COLLECTION_USERS)
                    .document(preferenceManager.getString(Constants.KEY_USER_ID))
                    .update("name", newName)
                    .addOnSuccessListener(OnSuccessListener {
                        binding.txtUserNameProfile.setText(newName)
                        preferenceManager.putString(Constants.KEY_NAME , newName)
                        dialog.dismiss()

                    })
            } else {
                Toast.makeText(this, "Failed Empty!", Toast.LENGTH_LONG).show()
            }

        }
        changeNameDilaogBinding.btnCancel.setOnClickListener {

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showChangePasswordDialog() {

        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val dialog = Dialog(this)

        dialog.setCancelable(true)

        val changeNameDilaogBinding: ChangeNameDilaogBinding =
            ChangeNameDilaogBinding.inflate(layoutInflater)
        dialog.setContentView(changeNameDilaogBinding.root)
        dialog.window!!.setLayout(6 * width / 7, 1 * height / 4)



        changeNameDilaogBinding.txtNewValue.setText("Enter Valid Email to Reset Password")
        changeNameDilaogBinding.btnDone.setOnClickListener {

            val email = changeNameDilaogBinding.inputNewValue.text.toString()
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    dialog.dismiss()
                }


        }
        changeNameDilaogBinding.btnCancel.setOnClickListener {

            dialog.dismiss()
        }

        dialog.show()
    }

}