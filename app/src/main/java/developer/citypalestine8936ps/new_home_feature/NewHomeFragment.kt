package developer.citypalestine8936ps.new_home_feature

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import developer.citypalestine8936ps.BuildConfig
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.databinding.FragmentNewHomeBinding
import developer.citypalestine8936ps.models.User
import developer.citypalestine8936ps.new_home_feature.comments.CommentsDialog
import developer.citypalestine8936ps.new_home_feature.posts.NewPost
import developer.citypalestine8936ps.new_home_feature.posts.NewPostAdapter
import developer.citypalestine8936ps.new_home_feature.posts.NewPostListener
import developer.citypalestine8936ps.new_home_feature.posts.PostModelForAdapter
import developer.citypalestine8936ps.new_image_preview.ImagePreviewDialog
import developer.citypalestine8936ps.utilites.Constants
import developer.citypalestine8936ps.utilites.DialogUtil
import developer.citypalestine8936ps.utilites.PreferenceManager
import java.io.File


class NewHomeFragment : Fragment(), View.OnClickListener, NewPostListener {
    private lateinit var binding: FragmentNewHomeBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private val postAdapter by lazy {
        NewPostAdapter(requireContext(), mutableListOf(), loggedUserId, this)
    }

    private lateinit var usersCollectionRef: CollectionReference
    private lateinit var postsCollectionRef: CollectionReference

    private lateinit var postStorageRef: StorageReference

    private var loggedUserId: String = ""
    private var postImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNewHomeBinding.inflate(inflater)
        return binding.apply {
            ibPost.setOnClickListener(this@NewHomeFragment)
            ibPickCamera.setOnClickListener(this@NewHomeFragment)
            ibPickGallery.setOnClickListener(this@NewHomeFragment)
            ibRemoveImage.setOnClickListener(this@NewHomeFragment)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
    }


    private fun initData() {
        database = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        usersCollectionRef = database.collection(Constants.KEY_COLLECTION_USERS)
        postsCollectionRef = database.collection(Constants.KEY_COLLECTION_NEW_POSTS)
        postStorageRef = storage.reference.child(Constants.KEY_POSTS_STORAGE_REF)

        // get logged user data
        loggedUserId = PreferenceManager(requireContext()).getString(Constants.KEY_USER_ID)
        usersCollectionRef.document(loggedUserId).get().addOnCompleteListener {
            if (!it.isSuccessful) {
                Toast.makeText(requireContext(), "Error Logged User ID", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            }
            val loggedUser = it.result.toObject(User::class.java)
            initLoggedUserData(loggedUser)
        }

        // subscribe to posts collection in DB
        postsCollectionRef.orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, _ ->
                snapshots?.documentChanges?.let {
                    for (doc in it) {
                        val newPost = doc.document.toObject(NewPost::class.java)
                        when (doc.type) {
                            DocumentChange.Type.ADDED -> onPostAdded(newPost)
                            DocumentChange.Type.MODIFIED -> onPostModified(newPost)
                            DocumentChange.Type.REMOVED -> onPostRemoved(newPost)
                        }
                    }
                }
            }

        // init adapter
        initPostsAdapter()
    }

    private fun initPostsAdapter() {
        binding.rvPosts.adapter = postAdapter
    }

    private fun onPostAdded(newPost: NewPost) {
        usersCollectionRef.document(newPost.authorDocId).get().addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }
            val data = it.result.toObject(User::class.java)
            data?.let { author ->
                postAdapter.addPost(PostModelForAdapter(author = author, post = newPost))
            }
        }
    }

    private fun onPostModified(newPost: NewPost) {
        postAdapter.modifyPost(newPost)
    }

    private fun onPostRemoved(newPost: NewPost) {
        postAdapter.removePost(newPost)
    }

    private fun initLoggedUserData(loggedUser: User?) {
        loggedUser?.let {
            binding.tvPostAuthorName.text = it.name
            if (loggedUser.image.isNotEmpty())
                Picasso.get().load(loggedUser.image).into(binding.ivPostAuthorImage)
        }
    }

    private fun onClickPost() {
        val postText = binding.etPostContent.text.toString()
        if (postText.isEmpty() && postImageUri == null) {
            Toast.makeText(requireContext(), "Can't post empty", Toast.LENGTH_SHORT).show()
            return
        }
        DialogUtil(requireContext()).showLoadingDialog(true, getString(R.string.posting))
        val doc = postsCollectionRef.document()

        if (postImageUri != null) {
            val imageName = "${doc.id}.png"
            postStorageRef.child(imageName).putFile(postImageUri!!).addOnCompleteListener {
                if (!it.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Something wont wrong, please try again later !!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addOnCompleteListener
                }
                postStorageRef.child(imageName).downloadUrl.addOnSuccessListener { uri ->
                    sendMessage(doc, postText, uri.toString())
                }
            }
        } else {
            sendMessage(doc, postText, "")
        }

    }


    private fun sendMessage(doc: DocumentReference, postText: String, imageUrl: String) {

        val post = NewPost(
            docId = doc.id,
            imageUrl = imageUrl,
            content = postText.toString(),
            authorDocId = loggedUserId
        )

        doc.set(post).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Post Successfully", Toast.LENGTH_SHORT).show()
                clearPostView()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Something wont wrong, please try again later",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun clearPostView() {
        postImageUri = null

        binding.etPostContent.text.clear()
        binding.cvImagePreview.visibility = View.GONE

        DialogUtil(requireContext()).showLoadingDialog(false)

    }

    private fun onClickPickCamera() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activityResultLauncher.launch(Manifest.permission.CAMERA)
        } else {
            lifecycleScope.launchWhenStarted {
                getTmpFileUri().let { uri ->
                    postImageUri = uri
                    takeImageResult.launch(uri)
                }
            }
        }
    }

    private fun onClickPickGallery() {
        selectImageFromGalleryResult.launch("image/*")

    }

    private fun onClickRemoveImage() {
        changeImagePreviewVisibility(false)
        postImageUri = null
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Handle Permission granted/rejected
            if (isGranted) {
                // Permission is granted
                onClickPickCamera()
            } else {
                // Permission is denied
                Toast.makeText(
                    requireContext(),
                    "Camera permission is required",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                postImageUri?.let { uri ->
                    changeImagePreviewVisibility(true)
                    binding.ivPostImage.setImageURI(uri)
                }
            }
        }


    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                changeImagePreviewVisibility(true)
                postImageUri = it
                binding.ivPostImage.setImageURI(it)
            }
        }


    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }

        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    private fun changeImagePreviewVisibility(status: Boolean) {
        binding.cvImagePreview.visibility = if (status) View.VISIBLE else View.GONE
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.ibPost -> onClickPost()
            binding.ibPickCamera -> onClickPickCamera()
            binding.ibPickGallery -> onClickPickGallery()
            binding.ibRemoveImage -> onClickRemoveImage()
        }
    }

    override fun onClickAuthorImage(post: NewPost) {
        TODO("Not yet implemented")
    }

    override fun onClickLike(post: NewPost) {
        val isLike = post.likes.contains(loggedUserId)
        if (isLike) {
            // Dislike (remove)
            post.likes.remove(loggedUserId)
        } else {
            // Like (add)
            post.likes.add(loggedUserId)
        }
        postsCollectionRef.document(post.docId).set(post)
    }

    override fun onClickImage(post: NewPost) {
        ImagePreviewDialog.display(
            fragmentManager = childFragmentManager,
            imageUrl = post.imageUrl
        )
    }

    override fun onClickComment(post: NewPost) {
        CommentsDialog.display(
            fragmentManager = childFragmentManager,
            postId = post.docId
        )
    }
}