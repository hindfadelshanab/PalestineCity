package developer.citypalestine8936ps.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.models.BookType


class BookTypeAdpter(private val mList: List<BookType> ,private val context: Context) :
    RecyclerView.Adapter<BookTypeAdpter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_type_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]

        holder.textViewType.setText(itemsViewModel.bookType)

        val layoutManager = LinearLayoutManager(
            holder.recycleview
                .getContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        layoutManager
            .setInitialPrefetchItemCount(
                itemsViewModel.books!!.size
            );

        val bookAdpter = BookAdpter(itemsViewModel.books!!, context)
        holder.recycleview
            .setLayoutManager(layoutManager)
        holder.recycleview
            .setAdapter(bookAdpter)
//        holder.recycleview
//            .setRecycledViewPool(viewPool)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewType: TextView = itemView.findViewById(R.id.txt_bookType)
        val recycleview: RecyclerView = itemView.findViewById(R.id.allBookRc);
    }
}
