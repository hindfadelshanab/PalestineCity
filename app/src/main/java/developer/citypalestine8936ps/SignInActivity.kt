package developer.citypalestine8936ps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import developer.citypalestine8936ps.databinding.ActivitySignInBinding
import developer.citypalestine8936ps.databinding.ActivitySignUpBinding
import developer.citypalestine8936ps.utilites.Constants
import developer.citypalestine8936ps.utilites.PreferenceManager

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private var preferenceManager: PreferenceManager? = null
    val TAG = "SignIn"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(applicationContext)
        if (preferenceManager!!.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        setListener()
    }

    private fun setListener() {
        binding.textCreateNewAccount.setOnClickListener { view ->
            startActivity(
                Intent(
                    applicationContext,
                    SignUpActivity::class.java
                )
            )
        }

        binding.textForgetPassword.setOnClickListener { view ->
            startActivity(
                Intent(
                    applicationContext,
                    ForgetPasswordActivity::class.java
                )
            )
        }
        binding.buttonSignIn.setOnClickListener { view ->
            if (isValidSignInDetails()) {
                signIn()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun signIn() {
        loading(true)
        val database = FirebaseFirestore.getInstance()
        val mAuth = FirebaseAuth.getInstance()


        mAuth.signInWithEmailAndPassword(binding.inputEmail.text.toString(), binding.inputPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = mAuth.currentUser
//                    Log.e("ErrorSSs" , preferenceManager!!.getString(Constants.KEY_USER_ID))

                    Log.e("ErrorSSs" , user!!.uid +"uiiiiiiiid")

                    database.collection(Constants.KEY_COLLECTION_USERS)
                        .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.text.toString())
                 //     .whereEqualTo(Constants.KEY_USER_ID, user.uid)
                        .get()
                        .addOnFailureListener(){ e:Exception  ->
                            showToast("fail" + e.message)

                        }
                        .addOnCompleteListener { task: Task<QuerySnapshot?> ->
                            if (task.isSuccessful && task.result != null && task.result!!
                                    .documents.size > 0
                            ) {
                                val documentSnapshot = task.result!!.documents[0]

                                preferenceManager!!.putString(Constants.KEY_USER_ID,documentSnapshot.id )

                                preferenceManager!!.putBoolean(Constants.KEY_IS_SIGNED_IN, true)

                                preferenceManager!!.putString(
                                    Constants.KEY_NAME,
                                    documentSnapshot.getString(Constants.KEY_NAME)
                                )
                                preferenceManager!!.putString(
                                    Constants.KEY_IMAGE,
                                    documentSnapshot.getString(Constants.KEY_IMAGE)
                                )
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)



                            } else {
                                loading(false)
                                showToast("Unable to sign in")
                            }

                        }


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    loading(false)

                }
            }

    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.buttonSignIn.setVisibility(View.INVISIBLE)
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.buttonSignIn.setVisibility(View.VISIBLE)
            binding.progressBar.visibility = View.INVISIBLE
        }
    }


    private fun isValidSignInDetails(): Boolean {
        return if (binding.inputEmail.text.toString().trim().isEmpty()) {
            showToast("Enter Email ")
            binding.inputEmail.setError("Enter Email")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.text.toString()).matches()) {
            showToast("Enter valid email")

            false
        } else if (binding.inputPassword.text.toString().trim().isEmpty()) {
            showToast("Enter Password")
            false
        } else {
            true
        }
    }
}