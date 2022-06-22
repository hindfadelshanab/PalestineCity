package developer.citypalestine8936ps

import android.content.Intent
import android.graphics.BitmapFactory
import android.location.*
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import developer.citypalestine8936ps.databinding.ActivityProfileBinding
import developer.citypalestine8936ps.models.User
import developer.citypalestine8936ps.utilites.Constants
import developer.citypalestine8936ps.utilites.PreferenceManager
import java.util.*


class ProfileActivity : AppCompatActivity() {

    private lateinit var database: FirebaseFirestore
    lateinit var binding: ActivityProfileBinding


    // private lateinit var mMap: GoogleMap
    lateinit var preferenceManager: PreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)
        database = FirebaseFirestore.getInstance()

        getSupportActionBar()?.setTitle("الملف الشخصي");


        getUserInfo(preferenceManager.getString(Constants.KEY_USER_ID))
        Picasso.get().load(preferenceManager.getString(Constants.KEY_IMAGE))
            .into(binding.profileImage)
//        val bytes: ByteArray =
//            Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT)
//        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//        binding.profileImage.setImageBitmap(bitmap)
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


}