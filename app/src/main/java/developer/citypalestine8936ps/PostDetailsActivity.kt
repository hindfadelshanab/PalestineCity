package developer.citypalestine8936ps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

import com.squareup.picasso.Picasso
import developer.citypalestine8936ps.adapters.CommentAdpter
import developer.citypalestine8936ps.databinding.ActivityPostDetailsBinding
import developer.citypalestine8936ps.models.Comment
import developer.citypalestine8936ps.models.Post
import developer.citypalestine8936ps.models.User
import developer.citypalestine8936ps.utilites.Constants
import developer.citypalestine8936ps.utilites.PreferenceManager

class PostDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityPostDetailsBinding
    lateinit var db:FirebaseFirestore
    private var preferenceManager: PreferenceManager? = null
    lateinit var commentAdpter: CommentAdpter
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceManager = PreferenceManager(this)
        var userId = preferenceManager!!.getString(Constants.KEY_USER_ID)
        db = FirebaseFirestore.getInstance()

        getSupportActionBar()?.setTitle("تفاصيل المنشور");


        var post = intent.getParcelableExtra<Post>("Post")
        if (post!!.postImage == "") {

            binding.imgPostImage.visibility = View.GONE

        } else {
            Picasso.get().load(post!!.postImage)

                .into(binding.imgPostImage)
        }
        if (post!!.userImage !== null && post!!.userImage !== "") {
            Picasso.get().load(post!!.userImage)
            //      .into(binding.imagePostUserProfile)

        }

        binding.txtNumberOfLike.setText(post!!.numberOfNum.toString())
        // binding.txtPostUserName.setText(post!!.teacherName.toString())
        //binding.txtPostClubName.setText(post!!.clubName.toString())
        binding.txtPostDescription.setText(post!!.postDec.toString())

        db.collection("Post").document(post.postId.toString()).collection("Comment").get()
            .addOnSuccessListener { docs ->

                Log.e("number of comment", "size : ${docs.size()}");
                binding.txtNumberOfComment.text = docs.size().toString()


            }

//        var user = User()
//
//        if (preferenceManager!!.getBoolean(Constants.KEY_IS_Teacher)){
//            db.collection(Constants.KEY_COLLECTION_TEACHER).document(userId).get().addOnSuccessListener { doc ->
//                user = doc.toObject(User::class.java)!!
//            }
//
//        }else if(preferenceManager!!.getBoolean(Constants.KEY_IS_Student)){
//            db.collection(Constants.KEY_COLLECTION_STUDENT).document(userId).get().addOnSuccessListener { doc ->
//                user = doc.toObject(User::class.java)!!
//            }
//
//        }
        db.collection(Constants.KEY_COLLECTION_USERS).document(userId).get()
            .addOnSuccessListener { doc ->
                user = doc.toObject(User::class.java)!!
                getAllComment(post, userId)


                binding.imagSendComment.setOnClickListener {
                    if (binding.inputComment.text.isEmpty()) {
                        binding.inputComment.setError("لا يوجد تعليق")
                        // binding.imagSendComment.is

                    } else {
                        //      binding.imagSendComment.visibility = View.VISIBLE
                        var comment = Comment()
                        comment.commentText = binding.inputComment.text.toString()
                        comment.userSendId = user.id
                        comment.userSendImage = user.image
                        comment.userSendName = user.name

                        addComment(post, comment, userId)
                        getAllComment(post, userId)
                    }
                }


            }
    }

    fun  addComment(post: Post, comment: Comment, userid: String){
        binding.imagSendComment.isEnabled=false

        val ref: DocumentReference = db.collection("Comment").document()
        comment.commentId = ref.id
//        ref.set(post).addOnSuccessListener {
//            Toast.makeText(this , "Comment send" , Toast.LENGTH_LONG).show()
//
//        }

        db.collection("Post").document(post.postId.toString()).collection("Comment").document()
            .set(comment)
            .addOnSuccessListener {
                Toast.makeText(this , "Comment Send" , Toast.LENGTH_LONG).show()
                binding.inputComment.text.clear()
                binding.imagSendComment.isEnabled=true

            }





    }

    fun getAllComment(post: Post , userid :String){
        var data = ArrayList<Comment>()
        db.collection("Post").document(post.postId.toString()).collection("Comment")
            .get().addOnSuccessListener { docs ->
                for (d in docs){
                    data.add(d.toObject(Comment::class.java))

                }

                binding.commentRc.visibility =View.VISIBLE
                commentAdpter = CommentAdpter(data )
                binding.commentRc.setAdapter(commentAdpter)
                binding.commentRc.setLayoutManager(LinearLayoutManager(this))
            }


    }
}