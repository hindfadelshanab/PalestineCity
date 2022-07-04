package developer.citypalestine8936ps.Fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import developer.citypalestine8936ps.BuildConfig
import developer.citypalestine8936ps.PostDetailsActivity
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.adapters.CityAdapter
import developer.citypalestine8936ps.adapters.PostAdpter
import developer.citypalestine8936ps.databinding.FragmentHomeBinding
import developer.citypalestine8936ps.listeners.PostListener
import developer.citypalestine8936ps.models.City
import developer.citypalestine8936ps.models.Post
import developer.citypalestine8936ps.models.User
import developer.citypalestine8936ps.utilites.Constants
import developer.citypalestine8936ps.utilites.PreferenceManager
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), PostListener {
    private lateinit var postAdpter: PostAdpter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseFirestore
    private var preferenceManager: PreferenceManager? = null
    private var latestTmpUri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        database = FirebaseFirestore.getInstance()
        preferenceManager = PreferenceManager(activity)


        val userId = preferenceManager!!.getString(Constants.KEY_USER_ID)
        val userImage = preferenceManager!!.getString(Constants.KEY_IMAGE)
        val userName = preferenceManager!!.getString(Constants.KEY_NAME)



        Picasso.get().load(userImage)
            .into(binding.imageUserSendPost)


        binding.txtNameUserSendPost.setText(userName)

        getAllPost(userImage, userId)
        binding.imageFromGallery.setOnClickListener { view ->
            selectImageFromGallery()
        }
        binding.imageFromCamera.setOnClickListener { view ->
            takeImage()
        }


        binding.imageSendPost.setOnClickListener(View.OnClickListener {
            if (binding.txtWritePost.text.isEmpty()) {
                binding.txtWritePost.setError("Enter your Post")
            } else {
                sendPost(userId, latestTmpUri ,userImage)
                getAllPost(userImage, userId)
            }
        })
        return binding.root;
    }

    private fun getAllPost(userImage: String, userId: String) {
        var data = ArrayList<Post>()
        binding.progressbar.visibility = View.VISIBLE

//        database.collection(Constants.KEY_COLLECTION_CITY).get()
//            .addOnSuccessListener { queryDocumentSnapshots ->
//                for (documentSnapshot in queryDocumentSnapshots) {
//                    documentSnapshot.id
                    database
                        .collection("Post")
                        .get().addOnSuccessListener { queryDocumentSnapshots1 ->
                            Log.e("hin5", "${queryDocumentSnapshots1.documents.size}  kdkkdkd")

                            for (documentSnapshot1 in queryDocumentSnapshots1) {
                                val post: Post = documentSnapshot1.toObject(Post::class.java)
                                Log.e("hin555", post.postDec.toString() + "")
                                Log.e("hin55", documentSnapshot1.id + "")

                                data.add(post)
                            }
                            postAdpter = PostAdpter(data, activity, userImage, userId, this)
                            binding.cityRec.setAdapter(postAdpter)

                            //    binding.p.visibility = View.GONE
                            binding.cityRec.visibility = View.VISIBLE
                            binding.progressbar.visibility = View.GONE
                            binding.cityRec.visibility = View.VISIBLE


                            Log.e("hin", data.size.toString() + "")
                            binding.cityRec.setLayoutManager(LinearLayoutManager(activity))

                        }


           //     }

      //     }
    }

    override fun onPostClicked(post: Post) {
        val intent = Intent(activity, PostDetailsActivity::class.java)
        intent.putExtra("Post", post)
        intent.putExtra("comment", post.numberOfComment)
        //   preferenceManager!!.putString(Constants.KEY_USER , post!!.id)

        startActivity(intent)
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTempLatestTmpUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->

                    //    uploadImage(uri)
                    binding.imageForPost.visibility = View.VISIBLE
                    binding.imageForPost.setImageURI(uri)
                }
            }
        }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.imageForPost.visibility = View.VISIBLE
                binding.imageForPost.setImageURI(it)
                latestTmpUri = it
            }
            Log.d("TAG", ": selectImageFromGalleryResult uri $uri")
            Log.d("TAG", ": selectImageFromGalleryResult latestTmpUri $latestTmpUri")
        }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun getTempLatestTmpUri(): Uri {
        val tmpFile =
            File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }

        return FileProvider.getUriForFile(
            requireContext(),
            "$6{BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }


    fun sendPost(userId: String, fileUri: Uri?,userimage: String) {

        binding.imageSendPost.isEnabled = false

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
                                post.likeBy = java.util.ArrayList()
                                post.userId = userId


                                val ref: DocumentReference =
                                    database.collection("Post")
                                        .document()
                                post.postId = ref.id
                                ref.set(post).addOnSuccessListener {
                                    binding.txtWritePost.text.clear()
                                    Toast.makeText(activity, "Post Send ", Toast.LENGTH_SHORT)
                                        .show()
                                    latestTmpUri = null
                                    binding.imageForPost.setImageBitmap(null)
                                    binding.imageForPost.visibility = View.GONE

                                    binding.imageSendPost.isEnabled = true

                                      getAllPost( userId,userimage)
                                }
                            }
                    }
                });
        } else {

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
                    post.userId = userId
                    post.isLike = false
                    post.likeBy = java.util.ArrayList()
                    //  post.cityId = cityId

                    //  database.collection("City").document(cityId).
                    val ref: DocumentReference = database.collection("Post")
                        .document()
                    post.postId = ref.id
                    ref.set(post).addOnSuccessListener {
                        binding.txtWritePost.text.clear()
                        Toast.makeText(activity, "Post Send ", Toast.LENGTH_SHORT)
                            .show()
                        latestTmpUri = null
                        binding.imageForPost.setImageBitmap(null)
                        binding.imageForPost.visibility = View.GONE

                        binding.imageSendPost.isEnabled = true
                        getAllPost( userId,userimage)

                    }
                }
        }


    }


}