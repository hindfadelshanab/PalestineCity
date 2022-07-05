package developer.citypalestine8936ps.new_home_feature.comments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.databinding.DialogCommentsBinding
import developer.citypalestine8936ps.models.User
import developer.citypalestine8936ps.utilites.Constants
import developer.citypalestine8936ps.utilites.PreferenceManager

class CommentsDialog : DialogFragment(), View.OnClickListener {
    private lateinit var binding: DialogCommentsBinding
    private lateinit var database: FirebaseFirestore

    private val commentAdapter by lazy {
        NewCommentAdapter(requireContext(), mutableListOf())
    }

    private var postDocId: String = ""
    private var loggedUserId: String = ""

    private lateinit var usersCollectionRef: CollectionReference
    private lateinit var postsCollectionRef: CollectionReference
    private lateinit var commentsCollectionRef: CollectionReference

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
        database = FirebaseFirestore.getInstance()

        loggedUserId = PreferenceManager(requireContext()).getString(Constants.KEY_USER_ID)

        usersCollectionRef = database.collection(Constants.KEY_COLLECTION_USERS)
        postsCollectionRef = database.collection(Constants.KEY_COLLECTION_NEW_POSTS)

        arguments?.let {
            postDocId = it.getString(Constants.KEY_POST_DOC_ID, "") ?: ""
            if (postDocId.isNotEmpty()) {
                commentsCollectionRef = postsCollectionRef
                    .document(postDocId)
                    .collection(Constants.KEY_COLLECTION_COMMENTS)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCommentsBinding.inflate(inflater)
        return binding.apply {
//            toolbar.title = getString(R.string.comments)
//            toolbar.setTitleTextColor(R.color.white)
//            toolbar.setTitleTextColor(android.graphics.Color.WHITE)
            toolbar.setNavigationOnClickListener { dismiss() }
            postComment.setOnClickListener(this@CommentsDialog)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: postDocId $postDocId")
        commentsCollectionRef
            .addSnapshotListener { snapshots, _ ->
                snapshots?.let {
                    for (dc in snapshots.documentChanges) {
                        val comment = dc.document.toObject(NewComment::class.java)
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> onCommentAdded(comment)
                            DocumentChange.Type.MODIFIED -> onCommentModified(comment)
                            DocumentChange.Type.REMOVED -> onCommentRemoved(comment)
                        }
                    }
                }
            }

        initCommentsList()
    }

    private fun initCommentsList() {
        binding.rvComments.adapter = commentAdapter
    }

    private fun onCommentAdded(comment: NewComment) {
        usersCollectionRef.document(comment.sendDocId).get()
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                }
                val data = it.result.toObject(User::class.java)
                data?.let { sender ->
                    val commentModelForAdapter = CommentModelForAdapter(comment, sender)
                    commentAdapter.addComment(commentModelForAdapter)
                }
            }
    }

    private fun onCommentModified(comment: NewComment) {
        commentAdapter.modifyComment(comment)
    }

    private fun onCommentRemoved(comment: NewComment) {
        commentAdapter.removeComment(comment)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.postComment -> onCLickPostComment()
        }
    }

    private fun onCLickPostComment() {
        val commentText = binding.etComment.text.toString()
        if (commentText.isEmpty()) {
            Toast.makeText(requireContext(), "Can't send empty comment !!", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val commentDoc = commentsCollectionRef.document()
        val comment = NewComment(
            docId = commentDoc.id,
            commentText = commentText,
            sendDocId = loggedUserId
        )
        commentDoc.set(comment).addOnCompleteListener {
            if (!it.isSuccessful) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.somthing_wrong),
                    Toast.LENGTH_SHORT
                ).show()
            }
            clearCommentView()
        }
    }

    private fun clearCommentView() {
        binding.etComment.text.clear()
    }

    companion object {
        private const val TAG = "CommentsDialog"
        fun display(fragmentManager: FragmentManager, postId: String = ""): CommentsDialog {
            val commentsDialog = CommentsDialog()
            if (postId.isNotEmpty()) {
                commentsDialog.arguments = bundleOf(Constants.KEY_POST_DOC_ID to postId)
            }
            commentsDialog.show(fragmentManager, TAG)
            return commentsDialog
        }
    }

}
