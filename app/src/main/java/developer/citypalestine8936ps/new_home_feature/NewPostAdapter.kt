package developer.citypalestine8936ps.new_home_feature

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.databinding.PostItemBinding
import developer.citypalestine8936ps.models.PostModelForAdapter

class NewPostAdapter(
    private val context: Context,
    private var posts: MutableList<PostModelForAdapter>,
    private val loggedUserId: String,
    private val postListener: NewPostListener,
) : RecyclerView.Adapter<NewPostAdapter.NewPostViewHolder>() {

    fun updateData(data: MutableList<PostModelForAdapter>) {
        posts = data
        notifyDataSetChanged()
    }

    fun addPost(newPost: PostModelForAdapter) {
        posts.add(newPost)
        notifyItemInserted(posts.size - 1)
    }

    fun updatePost(newPost: NewPost) {
        val postPosition = posts.indexOfFirst { it.post.docId == newPost.docId }
        val oldPost = posts[postPosition]
        posts[postPosition] = oldPost.copy(post = newPost)
        notifyItemChanged(postPosition)
    }

    fun removePost(newPost: NewPost) {
        val postPosition = posts.indexOfFirst { it.post.docId == newPost.docId }
        val removeResult = posts.removeIf { it.post.docId == newPost.docId }
        if (removeResult)
            notifyItemRemoved(postPosition)
    }

    inner class NewPostViewHolder(private val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(modelForAdapter: PostModelForAdapter) {
            val post = modelForAdapter.post
            binding.txtPostUserName.text = modelForAdapter.author.name
            Picasso.get().load(modelForAdapter.author.image).into(binding.imagePostUserPhoto)

            /*Optional Post Data*/
            if (post.content.isNotEmpty()) {
                binding.txtPostDescription.visibility = View.VISIBLE
                binding.txtPostDescription.text = post.content
            } else {
                binding.txtPostDescription.visibility = View.GONE
            }

            if (post.imageUrl.isNotEmpty()) {
                binding.imgPostImage.visibility = View.VISIBLE
                Picasso.get().load(post.imageUrl).into(binding.imgPostImage)
            } else {
                binding.imgPostImage.visibility = View.GONE
            }

            /*Counters*/
            binding.tvPostLikeCount.text = when (val likes = post.likes.size) {
                0 -> {
                    context.getString(R.string.no_likes_yet)
                }
                1 -> {
                    context.getString(R.string.like_count, likes)
                }
                else -> {
                    context.getString(R.string.likes_count, likes)
                }
            }

            /*Like Button*/
            binding.imgLike.setImageResource(
                if (modelForAdapter.post.likes.contains(loggedUserId)) {
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
                postListener.onClickLike(post)
            }

            binding.imgShare.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, post.content)
                intent.type = "text/plain"
                context.startActivity(Intent.createChooser(intent, "Share To:"))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewPostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostItemBinding.inflate(inflater, parent, false)
        return NewPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewPostViewHolder, position: Int) {
        val currentPost = posts[position]
        holder.bindItem(currentPost)
    }

    override fun getItemCount(): Int = posts.size
}