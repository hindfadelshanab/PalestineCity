package developer.citypalestine8936ps.new_city_feature.landmarks

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.citypalestine8936ps.databinding.ItemCityPhotoBinding
import developer.citypalestine8936ps.utilites.load

class LandmarkPhotoAdapter(
    private val context: Context,
    private var photos: List<String>,
    private var listener: LandmarkListener
) : RecyclerView.Adapter<LandmarkPhotoAdapter.LandmarkPhotoViewHolder>() {
    fun updateData(data: List<String>) {
        photos = data
        notifyDataSetChanged()
    }


    inner class LandmarkPhotoViewHolder(private val binding: ItemCityPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(photo: String) {
            binding.root.setOnClickListener { listener.onClickLandMarkPhoto(photo) }
            binding.ivCityPhoto.load(context = context, url = photo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandmarkPhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCityPhotoBinding.inflate(inflater, parent, false)
        return LandmarkPhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LandmarkPhotoViewHolder, position: Int) {
        val currentPhoto = photos[position]
        holder.bindItem(currentPhoto)
    }

    override fun getItemCount(): Int = /*if (photos.size > 6) 6 else */photos.size

}