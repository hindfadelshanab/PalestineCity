package developer.citypalestine8936ps

import android.R
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import developer.citypalestine8936ps.databinding.ActivitySignUpBinding
import developer.citypalestine8936ps.models.City
import developer.citypalestine8936ps.utilites.Constants
import developer.citypalestine8936ps.utilites.PreferenceManager
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var encodedImage: String? = null
    private var preferenceManager: PreferenceManager? = null
    val TAG = "SignUp"
    var data: ArrayList<City> = ArrayList<City>()
    lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceManager = PreferenceManager(applicationContext)
        database = FirebaseFirestore.getInstance()
        getAllCity()
        setListener()
    }

    private fun getAllCity() {


        var cityNames: ArrayList<String> = ArrayList()
        database.collection(Constants.KEY_COLLECTION_CITY).get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val city: City = documentSnapshot.toObject(City::class.java)
                    city.id = documentSnapshot.id

                    data.add(city)
                    cityNames.add(city.cityName)


                }

                val adapter: ArrayAdapter<City> = ArrayAdapter<City>(
                    this,
                    R.layout.simple_spinner_item, data
                )
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                binding.inputCity.setAdapter(adapter)


                binding.inputCity.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        val city: City = parent.selectedItem as City
                        displayUserData(city)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                })

//                //val areaSpinner = findViewById<View>(R.id.inputCity) as Spinner
//                val areas = ArrayList<String>(cityNames)
//                val areasAdapter =
//                    ArrayAdapter(this, R.layout.simple_spinner_item, areas)
//                areasAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
//                binding.inputCity.adapter = areasAdapter
            }).addOnFailureListener(OnFailureListener { e ->
                Log.e("hind", e.message!!)
            })


    }
    fun getSelectedUser(v: View?) {
        val user: City = binding.inputCity.getSelectedItem() as City
        displayUserData(user)
    }

    private fun displayUserData(city: City) {
        val name: String = city.cityName
        val age: Double = city.lng
//        val mail: String = user.getMail()
        val userData = "Name: $name\nAge: $age"
        Toast.makeText(this, userData, Toast.LENGTH_LONG).show()
    }

    private fun setListener() {

        binding.textSignIn.setOnClickListener { view -> onBackPressed() }
        binding.buttonSignUp.setOnClickListener { view ->
            if (isValidSignUpDetails() == true) {
                signUp()
            }
        }
        binding.layoutImage.setOnClickListener { view ->
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickImage.launch(intent)
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun signUp() {
        loading(true)
        val database = FirebaseFirestore.getInstance()
        val mAuth = FirebaseAuth.getInstance()


        mAuth.createUserWithEmailAndPassword(
            binding.inputEmail.text.toString(),
            binding.inputPassword.text.toString()
        )
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val currentUser: FirebaseUser? = mAuth.getCurrentUser()
                        val user = HashMap<String, Any>()
                        user[Constants.KEY_NAME] = binding.inputName.text.toString()
                        user[Constants.KEY_EMAIL] = binding.inputEmail.text.toString()
                        user[Constants.KEY_PASSWORD] = binding.inputPassword.text.toString()
                        user[Constants.KEY_CITY] = binding.inputCity.selectedItem.toString()
                        user[Constants.KEY_IMAGE] = encodedImage!!
                        database.collection(Constants.KEY_COLLECTION_USERS)
                            .add(user)
                            .addOnSuccessListener { documentReference: DocumentReference ->
                                loading(false)
                                preferenceManager!!.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
                                preferenceManager!!.putString(
                                    Constants.KEY_USER_ID,
                                    documentReference.id
                                )
                                preferenceManager!!.putString(
                                    Constants.KEY_NAME,
                                    binding.inputName.text.toString()
                                )
                                preferenceManager!!.putString(Constants.KEY_IMAGE, encodedImage)

                                Log.e("hind", "Sucees");
                                Log.e("hind", documentReference.id);
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                            }
                            .addOnFailureListener { e: Exception ->
                                loading(false)
                                showToast(e.message)

                            }
                        //  updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.

                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        //   updateUI(null)
                    }
                })

    }

    private fun encodImage(bitmap: Bitmap): String {
        val preViewWidth = 150
        val preViewHeight = bitmap.height * preViewWidth / bitmap.width
        val preViewBitmap = Bitmap.createScaledBitmap(bitmap, preViewWidth, preViewHeight, false)
        val byteArrayOutputStream = ByteArrayOutputStream()
        preViewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private val pickImage = registerForActivityResult(
        StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            if (result.data != null) {
                val imageUri = result.data!!.data
                try {
                    val inputStream =
                        contentResolver.openInputStream(imageUri!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.imageProfile.setImageBitmap(bitmap)
                    binding.textAddImage.visibility = View.GONE
                    encodedImage = encodImage(bitmap)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun isValidSignUpDetails(): Boolean? {

        return if (encodedImage == null) {
            showToast("Select profile image")

            false
        } else if (binding.inputName.text.toString().trim().isEmpty()) {

            binding.inputName.setError("Enter name")
            false
        } else if (binding.inputEmail.text.toString().trim().isEmpty()) {
            showToast("Enter email")
            binding.inputEmail.setError("Enter email")

            false
//        } else if (binding.inputCity.text.toString().trim().isEmpty()) {
//            showToast("Enter City")
//            binding.inputCity.setError("Enter City")
//
//            false
//        }
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.text.toString()).matches()) {
            showToast("Enter valid email")
            binding.inputEmail.setError("Enter valid email")

            false
        } else if (binding.inputPassword.text.toString().trim().isEmpty()) {
            showToast("Enter password")
            binding.inputPassword.setError("Enter password")

            false
        } else if (binding.inputConfirmPassword.text.toString().trim().isEmpty()) {
            showToast("Enter Confirm Password")
            binding.inputPassword.setError("Enter Confirm Password")

            false
        } else if (!binding.inputPassword.text.toString()
                .equals(binding.inputConfirmPassword.text.toString())
        ) {
            showToast("Password & Confirm Password must be same")
            false
        } else {
            true
        }
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.buttonSignUp.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.buttonSignUp.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}