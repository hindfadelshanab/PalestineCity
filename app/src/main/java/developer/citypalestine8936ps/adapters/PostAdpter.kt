package developer.citypalestine8936ps.adapters


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.databinding.PostItemBinding
import developer.citypalestine8936ps.listeners.PostListener
import developer.citypalestine8936ps.models.Post
import developer.citypalestine8936ps.utilites.Constants


class PostAdpter(
    private val mList: List<Post>,
    var context: FragmentActivity?,
    var userImage: String,
    var userId: String,
    var postListener: PostListener
) : RecyclerView.Adapter<PostAdpter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPost = mList[position]

        holder.textViewDec.text = currentPost.postDec
        holder.textViewUserName.text = currentPost.userName

        if (userId == "") {
            holder.textViewNumberOflike.visibility = View.GONE
            holder.likeImage.visibility = View.GONE
        }
        if (currentPost.likeBy!!.contains(userId)) {
            holder.likeImage.setImageResource(R.drawable.ic__fill_heart)
        } else {
            holder.likeImage.setImageResource(R.drawable.ic_heart)
        }



        holder.imageShare.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, currentPost.postDec)
            intent.type = "text/plain"
            context?.startActivity(Intent.createChooser(intent, "Share To:"))
        }

//        holder.textViewNumberOflike.text = "${itemsViewModel.numberOfNum.toString()} اعجاب"
        holder.tvPostLikeCount.text = when (currentPost.numberOfNum) {
            0 -> {
                context!!.getString(R.string.no_likes_yet)
            }
            1 -> {
                context!!.getString(R.string.like_count, currentPost.numberOfNum)
            }
            else -> {
                context!!.getString(R.string.likes_count, currentPost.numberOfNum)
            }
        }

        when (currentPost.numberOfComment) {
            0 -> {
                holder.tvPostCommentCount.visibility = View.GONE
            }
            1 -> {
                holder.tvPostCommentCount.visibility = View.VISIBLE
                holder.tvPostCommentCount.text =
                    context!!.getString(R.string.comment_count, currentPost.numberOfComment)
            }
            else -> {
                holder.tvPostCommentCount.visibility = View.VISIBLE
                holder.tvPostCommentCount.text =
                    context!!.getString(R.string.comment_count, currentPost.numberOfComment)
            }
        }

        holder.itemView.setOnClickListener { view ->
            postListener.onClickComment(currentPost)
        }
        holder.linearComment.setOnClickListener { view ->
            postListener.onClickComment(currentPost)
        }

        holder.linearLike.setOnClickListener {
            if (!currentPost.likeBy!!.contains(userId)) {
                Log.e("hhhdhhdhhdhd", userId + "hshhs");
                currentPost.likeBy!!.add(userId);
                holder.likeImage.setImageResource(R.drawable.ic__fill_heart)
            } else {
                // TODO: Dislike
                currentPost.likeBy!!.remove(userId);
                holder.likeImage.setImageResource(R.drawable.ic_heart)
            }

            val post = Post()
            post.postDec = currentPost.postDec
            post.postId = currentPost.postId
            post.postImage = currentPost.postImage
            post.likeBy = currentPost.likeBy
            post.isLike = currentPost.likeBy?.contains(userId) ?: false
            post.cityId = currentPost.cityId
            post.numberOfComment = currentPost.numberOfComment
            post.numberOfNum = currentPost.likeBy!!.size
            post.userImage = currentPost.userImage

            db.collection(Constants.KEY_COLLECTION_CITY)
                .document(currentPost.cityId.toString())
                .collection("Post")
                .document(currentPost.postId.toString())
                .set(post).addOnSuccessListener {
                    holder.tvPostLikeCount.text = when (post.likeBy!!.size) {
                        0 -> {
                            context!!.getString(R.string.no_likes_yet)
                        }
                        1 -> {
                            context!!.getString(R.string.like_count, currentPost.numberOfNum)
                        }
                        else -> {
                            context!!.getString(R.string.likes_count, currentPost.numberOfNum)
                        }
                    }
//                    holder.textViewNumberOflike.text = "${post.likeBy!!.size.toString()}اعجاب "
                }
        }



        if (currentPost.postImage == "" || currentPost.postImage == null) {
            holder.imageView.visibility = View.GONE
        } else if (currentPost.postImage != null) {
            Picasso.get()
                .load(currentPost.postImage)
                .into(holder.imageView, object : Callback {
                    override fun onSuccess() {
                        holder.imageView.visibility = View.VISIBLE

                    }

                    override fun onError(e: Exception?) {

                    }
                })

        }


















        Picasso.get()
            .load(currentPost.userImage)
            .into(holder.imageViewUser)
//
//        val bytes: ByteArray =
//            Base64.decode(userImage, Base64.DEFAULT)
//        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//        holder.imageViewUser.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageViewUser: ImageView = itemView.findViewById(R.id.image_post_user_photo)
        val textViewDec: TextView = itemView.findViewById(R.id.txt_post_description)
        val textViewUserName: TextView = itemView.findViewById(R.id.txt_post_user_name)
        val imageView: ImageView = itemView.findViewById(R.id.img_postImage)
        val textViewClubName: TextView = itemView.findViewById(R.id.txt_post_club_name)
        val textViewNumberOflike: TextView = itemView.findViewById(R.id.txt_numberOfLike)
        val tvPostLikeCount: TextView = itemView.findViewById(R.id.tvPostLikeCount)
        val tvPostCommentCount: TextView = itemView.findViewById(R.id.tvPostCommentCount)
        val textViewNumberOfComment: TextView = itemView.findViewById(R.id.txt_numberOfComment)
        val imageShare: LinearLayout = itemView.findViewById(R.id.layoyt_share)
        val linearComment: LinearLayout = itemView.findViewById(R.id.linearComment);
        val linearLike: LinearLayout = itemView.findViewById(R.id.linearLike);
        val likeImage: ImageView = itemView.findViewById<ImageView>(R.id.img_like)

    }

    inner class PostViewHolder(private val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindPost(post: Post) {
            /*Post Data*/
            binding.txtPostUserName.text = post.userName
            Picasso.get().load(post.userImage).into(binding.imagePostUserPhoto)


            /*Optional Post Data*/
            binding.txtPostDescription.text = post.postDec
            Picasso.get().load(post.postImage).into(binding.imgPostImage)

            /*Counters*/
            binding.tvPostLikeCount.text = when (post.numberOfNum) {
                0 -> {
                    context!!.getString(R.string.no_likes_yet)
                }
                1 -> {
                    context!!.getString(R.string.like_count, post.numberOfNum)
                }
                else -> {
                    context!!.getString(R.string.likes_count, post.numberOfNum)
                }
            }
            when (post.numberOfComment) {
                0 -> {
                    binding.tvPostCommentCount.visibility = View.GONE
                }
                1 -> {
                    binding.tvPostCommentCount.visibility = View.VISIBLE
                    binding.tvPostCommentCount.text =
                        context!!.getString(R.string.comment_count, post.numberOfComment)
                }
                else -> {
                    binding.tvPostCommentCount.visibility = View.VISIBLE
                    binding.tvPostCommentCount.text =
                        context!!.getString(R.string.comment_count, post.numberOfComment)
                }
            }

            /*Like Button*/
            binding.imgLike.setImageResource(
                if (!post.likeBy!!.contains(userId)) {
                    R.drawable.ic__fill_heart
                } else {
                    R.drawable.ic_heart
                }
            )

            /*Listeners*/
            binding.root.setOnClickListener {
                postListener.onClickComment(post)
            }
            binding.linearLike.setOnClickListener {
                postListener.onClickComment(post)
            }

            binding.imgShare.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, post.postDec)
                intent.type = "text/plain"
                context?.startActivity(Intent.createChooser(intent, "Share To:"))
            }
        }


    }
}
