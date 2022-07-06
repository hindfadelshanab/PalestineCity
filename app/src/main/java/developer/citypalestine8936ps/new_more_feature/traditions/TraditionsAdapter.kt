package developer.citypalestine8936ps.new_more_feature.traditions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.citypalestine8936ps.databinding.ItemTraditionBinding

class TraditionsAdapter(
    private val context: Context,
    private var traditions: List<TraditionData>,
    private val listener: TraditionalListener
) : RecyclerView.Adapter<TraditionsAdapter.TraditionsAdapterViewHolder>() {

    fun updateData(data: List<TraditionData>) {
        traditions = data
        notifyDataSetChanged()
    }

    inner class TraditionsAdapterViewHolder(private val binding: ItemTraditionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(traditionData: TraditionData) {
            binding.tvTraditionName.text = traditionData.name
            val traditionPhotoAdapter =
                TraditionPhotoAdapter(context, traditionData.photos, listener)
            binding.rvTraditionPhotos.adapter = traditionPhotoAdapter

            binding.tvTraditionShowMorePhotos.visibility = if (traditionData.photos.size < 6) {
                View.GONE
            } else {
                View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TraditionsAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTraditionBinding.inflate(inflater, parent, false)
        return TraditionsAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TraditionsAdapterViewHolder, position: Int) {
        val currentTradition = traditions[position]
        holder.bindItem(currentTradition)
    }

    override fun getItemCount(): Int = traditions.size
}