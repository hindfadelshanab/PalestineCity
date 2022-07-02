package developer.citypalestine8936ps.CityActivity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import developer.citypalestine8936ps.PostDetailsActivity
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.adapters.PostAdpter
import developer.citypalestine8936ps.databinding.ActivityPostBinding
import developer.citypalestine8936ps.listeners.PostListener
import developer.citypalestine8936ps.models.City
import developer.citypalestine8936ps.models.Post
import developer.citypalestine8936ps.models.User
import developer.citypalestine8936ps.utilites.Constants
import developer.citypalestine8936ps.utilites.PreferenceManager
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.util.*

class PostActivity : AppCompatActivity()  ,PostListener{

    lateinit var binding: ActivityPostBinding
    private lateinit var database: FirebaseFirestore
    private var preferenceManager: PreferenceManager? = null
    lateinit var postAdpter: PostAdpter
    lateinit var city: City
    private var encodedImage: Uri? = null
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        city = intent.getSerializableExtra("cityObj") as City
        preferenceManager = PreferenceManager(this)
        database = FirebaseFirestore.getInstance()

      var userId =  preferenceManager!!.getString(Constants.KEY_USER_ID)
      var userImage =  preferenceManager!!.getString(Constants.KEY_IMAGE)
        getAllPost(city.id , userImage ,userId)
//        binding.btnSendPost.setOnClickListener{
//            sendPost(city.id , userId , userImage)
//        }


        binding.txtWritePost.addTextChangedListener { charSequence  ->
            if (charSequence !=null){
                binding.imageSendPost.visibility=View.VISIBLE

                binding.imageSendPost.setOnClickListener(View.OnClickListener {
                    if (binding.txtWritePost.text.isEmpty()) {
                        binding.txtWritePost.setError("Enter your Post")
                    } else {
                        sendPost(city.id, userId , userImage , encodedImage)
                        getAllPost(city.id ,userImage , userId )
                    }
                })
            }else{
                binding.imageSendPost.visibility=View.GONE

            }

        }
      //  binding.txtWritePost.addTextChangedListener {  }

//        binding.imageSendPost.setOnClickListener(View.OnClickListener {
//            if (binding.txtWritePost.text.isEmpty()) {
//                binding.txtWritePost.setError("Enter your Post")
//            } else {
//                sendPost(city.id, userId , userImage , encodedImage!!)
//                getAllPost(city.id ,userImage )
//            }
//        })

        binding.imageFromGallery.setOnClickListener { view ->
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickImage.launch(intent)
        }


    }

    fun sendPost(cityId: String, userId: String , userImage: String , fileUri :Uri?) {



        binding.imageSendPost.isEnabled=false

        if (fileUri != null) {
            val fileName = UUID.randomUUID().toString() + ".jpg"
            val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")
            refStorage.putFile(fileUri)
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        var imageUrl = it.toString()

                        database.collection(Constants.KEY_COLLECTION_USERS).document(userId).get()
                            .addOnSuccessListener { doc ->
                                var user = doc.toObject(User::class.java)

                                var post: Post = Post()
                                post.postDec = binding.txtWritePost.text.toString()
                                post.userImage = user!!.image
                                post.userName = user!!.name
                                post.postImage = imageUrl
                                post.numberOfComment = 0
                                post.numberOfNum = 0
                                post.isLike = false
                                post.likeBy = ArrayList()



                                val ref: DocumentReference =
                                    database.collection("City").document(cityId).collection("Post")
                                        .document()
                                post.postId = ref.id
                                ref.set(post).addOnSuccessListener {
                                    binding.txtWritePost.text.clear()
                                    Toast.makeText(this, "Post Send ", Toast.LENGTH_SHORT)
                                        .show()
                                    encodedImage = null
                                    binding.imageForPost.setImageBitmap(null)
                                    binding.imageForPost.visibility =View.GONE

                                    binding.imageSendPost.isEnabled=true

                                    getAllPost(cityId  , userImage ,userId)
                                }
                            }
                    }
                });
        }else{

            database.collection(Constants.KEY_COLLECTION_USERS).document(userId).get()
                .addOnSuccessListener { doc ->
                    var user = doc.toObject(User::class.java)

                    var post: Post = Post()
                    post.postDec = binding.txtWritePost.text.toString()
                    post.userImage = user!!.image
                    post.userName = user!!.name
                    post.postImage = ""
                    post.numberOfComment = 0
                    post.numberOfNum = 0
                    post.isLike = false
                    post.likeBy = ArrayList()
                    post.cityId = cityId


                    val ref: DocumentReference =
                        database.collection("City").document(cityId).collection("Post")
                            .document()
                    post.postId = ref.id
                    ref.set(post).addOnSuccessListener {
                        binding.txtWritePost.text.clear()
                        Toast.makeText(this, "Post Send ", Toast.LENGTH_SHORT)
                            .show()
                        encodedImage = null
                        binding.imageForPost.setImageBitmap(null)
                        binding.imageForPost.visibility =View.GONE

                        binding.imageSendPost.isEnabled=true


                        getAllPost(cityId  , userImage , userId)
                    }
                }
        }












    }

    private fun getAllPost(cityId: String , userImage: String , userId:String) {
        var data = ArrayList<Post>()
        binding.progressbarPost.visibility = View.VISIBLE
        data.clear()
        database.collection("City").document(cityId).collection("Post")

            .get().addOnSuccessListener { queryDocumentSnapshots ->


                for (documentSnapshot in queryDocumentSnapshots) {
                    val post: Post = documentSnapshot.toObject(Post::class.java)
                    data.add(post)
                }
                postAdpter = PostAdpter(data, this , userImage ,userId , this)
                binding.postRc.setAdapter(postAdpter)

                binding.progressbarPost.visibility = View.GONE
                binding.postRc.visibility = View.VISIBLE

                Log.e("hin", data.size.toString() + "")
                binding.postRc.setLayoutManager(LinearLayoutManager(this))
            }
            .addOnFailureListener {
                binding.progressbarPost.visibility = View.VISIBLE
                binding.postRc.visibility = View.GONE
            }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            encodedImage = getImageUri(this, imageBitmap)
            if (encodedImage !=null) {
                binding.imageForPost.visibility =View.VISIBLE
                binding.imageForPost.setImageBitmap(imageBitmap)
            }

        }
    }
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }
    private val pickImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            if (result.data != null) {
                val imageUri = result.data!!.data
                try {
                    val inputStream = this?.contentResolver?.openInputStream(imageUri!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    //binding.imageProfile.setImageBitmap(bitmap)
                    //       binding.textAddImage.visibility = View.GONE
                    encodedImage = result.data!!.data!!
                    if (encodedImage !=null) {
                        binding.imageForPost.visibility =View.VISIBLE
                        binding.imageForPost.setImageBitmap(bitmap)
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onPostClicked(post: Post) {
        val intent = Intent(this  , PostDetailsActivity::class.java)
        intent.putExtra("Post", post)
        intent.putExtra("comment", post.numberOfComment)
        //   preferenceManager!!.putString(Constants.KEY_USER , post!!.id)

        startActivity(intent)
    }

    override fun onClickLike(post: Post) {
        TODO("Not yet implemented")
    }
}