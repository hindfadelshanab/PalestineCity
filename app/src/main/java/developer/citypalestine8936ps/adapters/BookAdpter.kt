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


class BookAdpter(private val mList: List<Book>) :
    RecyclerView.Adapter<BookAdpter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]
        holder.textViewName.setText(itemsViewModel.bookName)
        Picasso.get().load(itemsViewModel.bookPhoto).into(holder.imageview)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewName: TextView = itemView.findViewById(R.id.txt_book_name)
        val imageview: ImageView = itemView.findViewById(R.id.image_book_photo);
    }
}
