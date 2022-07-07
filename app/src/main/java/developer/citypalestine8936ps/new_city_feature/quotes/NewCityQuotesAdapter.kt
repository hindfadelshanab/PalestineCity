package developer.citypalestine8936ps.new_city_feature.quotes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.citypalestine8936ps.databinding.ItemCityQuoteBinding

class NewCityQuotesAdapter(
    private var quotes: List<String>,
    private val listener: QuoteListener,
) : RecyclerView.Adapter<NewCityQuotesAdapter.NewCityQuotesViewHolder>() {

    fun updateData(data: List<String>) {
        quotes = data
        notifyDataSetChanged()
    }

    inner class NewCityQuotesViewHolder(private val binding: ItemCityQuoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(quote: String) {
            binding.tvCityQuote.text = quote
            binding.ibCopyQuote.setOnClickListener { listener.onClickCopy(quote) }
            binding.ibShareQuote.setOnClickListener { listener.onClickShare(quote) }
            binding.ibShareQuoteWhatsapp.setOnClickListener { listener.onClickShareWhatsapp(quote) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewCityQuotesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCityQuoteBinding.inflate(inflater, parent, false)
        return NewCityQuotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewCityQuotesViewHolder, position: Int) {
        val currentQuote = quotes[position]
        holder.bindItem(currentQuote)
    }

    override fun getItemCount(): Int = quotes.size
}