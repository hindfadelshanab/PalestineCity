package developer.citypalestine8936ps.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.squareup.picasso.Picasso
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.models.Comment

class CommentAdpter(private val mList: List<Comment>) : RecyclerView.Adapter<CommentAdpter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]
        holder.textViewDec.setText(itemsViewModel.commentText)
        holder.txtStudentName.setText(itemsViewModel.userSendName)

        Picasso.get().load(itemsViewModel.userSendImage)
            .into(holder.imageView)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_sender_comment)
        val textViewDec: TextView = itemView.findViewById(R.id.txt_comment)
        val txtStudentName: TextView = itemView.findViewById(R.id.txt_user_name_comment)
    }
}
