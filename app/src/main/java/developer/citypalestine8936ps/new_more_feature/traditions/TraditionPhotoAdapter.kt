package developer.citypalestine8936ps.new_more_feature.traditions

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.citypalestine8936ps.databinding.ItemCityPhotoBinding
import developer.citypalestine8936ps.utilites.load

class TraditionPhotoAdapter(
    private val context: Context,
    private var photos: List<String>,
    private val listener: TraditionalListener
) : RecyclerView.Adapter<TraditionPhotoAdapter.TraditionPhotoViewHolder>() {
    fun updateData(data: List<String>) {
        photos = data
        notifyDataSetChanged()
    }


    inner class TraditionPhotoViewHolder(private val binding: ItemCityPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(photo: String) {
            binding.root.setOnClickListener { listener.onClickTraditionPhoto(photo) }
            binding.ivCityPhoto.load(context = context, url = photo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TraditionPhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCityPhotoBinding.inflate(inflater, parent, false)
        return TraditionPhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TraditionPhotoViewHolder, position: Int) {
        val currentPhoto = photos[position]
        holder.bindItem(currentPhoto)
    }

    override fun getItemCount(): Int = /*if (photos.size > 6) 6 else */photos.size

}