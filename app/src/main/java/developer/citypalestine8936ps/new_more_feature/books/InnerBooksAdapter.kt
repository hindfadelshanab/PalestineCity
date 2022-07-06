package developer.citypalestine8936ps.new_more_feature.books

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.citypalestine8936ps.databinding.ItemBookPhotoBinding
import developer.citypalestine8936ps.utilites.load

class InnerBooksAdapter(
    private val context: Context,
    private var books: List<InnerBook>
) : RecyclerView.Adapter<InnerBooksAdapter.TraditionPhotoViewHolder>() {
    fun updateData(data: List<InnerBook>) {
        books = data
        notifyDataSetChanged()
    }


    inner class TraditionPhotoViewHolder(private val binding: ItemBookPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(book: InnerBook) {
            binding.tvBookName.text = book.bookName
            binding.ivBookPhoto.load(context = context, url = book.bookPhoto)

            binding.root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(book.bookUrl), "application/pdf")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TraditionPhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBookPhotoBinding.inflate(inflater, parent, false)
        return TraditionPhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TraditionPhotoViewHolder, position: Int) {
        val currentBook = books[position]
        holder.bindItem(currentBook)
    }

    override fun getItemCount(): Int = /*if (photos.size > 6) 6 else */books.size

}