package developer.citypalestine8936ps.new_more_feature.books

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.citypalestine8936ps.databinding.ItemBookBinding

class BooksAdapter(
    private val context: Context,
    private var books: MutableList<BookData>
) : RecyclerView.Adapter<BooksAdapter.BooksAdapterViewHolder>() {

    fun updateData(data: MutableList<BookData>) {
        books = data
        notifyDataSetChanged()
    }

    fun insertBook(bookData: BookData) {
        books.add(bookData)
        notifyItemInserted(books.size - 1)
    }

    inner class BooksAdapterViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(book: BookData) {
            binding.tvBooksCategory.text = book.bookType
            val traditionPhotoAdapter = InnerBooksAdapter(context, book.books)
            binding.rvTraditionPhotos.adapter = traditionPhotoAdapter

            binding.tvBookShowMorePhotos.visibility = if (book.books.size < 6) {
                View.GONE
            } else {
                View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBookBinding.inflate(inflater, parent, false)
        return BooksAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BooksAdapterViewHolder, position: Int) {
        val currentBook = books[position]
        holder.bindItem(currentBook)
    }

    override fun getItemCount(): Int = books.size
}