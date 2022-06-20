package developer.citypalestine8936ps.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.models.Book
import developer.citypalestine8936ps.models.BookType


class PhotoAdpter(private val mList: List<String>) :
    RecyclerView.Adapter<PhotoAdpter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]
        Picasso.get().load(itemsViewModel).resize(200 ,200).into(holder.imageview)



    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
      //  val textViewName: TextView = itemView.findViewById(R.id.txt_book_name)
        val imageview: ImageView = itemView.findViewById(R.id.photo_p)
    }
}
