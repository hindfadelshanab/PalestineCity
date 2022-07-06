package developer.citypalestine8936ps.new_city_feature.photos

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.citypalestine8936ps.databinding.ItemCityPhotoBinding
import developer.citypalestine8936ps.utilites.load

class NewCityPhotoAdapter(
    private val context: Context,
    private var photos: List<String>
) : RecyclerView.Adapter<NewCityPhotoAdapter.NewCityPhotoViewHolder>() {

    fun updateData(data: List<String>) {
        photos = data
        notifyDataSetChanged()
    }

    inner class NewCityPhotoViewHolder(private val binding: ItemCityPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(photo: String) {
            binding.ivCityPhoto.load(context = context, url = photo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewCityPhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCityPhotoBinding.inflate(inflater, parent, false)
        return NewCityPhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewCityPhotoViewHolder, position: Int) {
        val currentPhoto = photos[position]
        holder.bindItem(currentPhoto)
    }

    override fun getItemCount(): Int = photos.size

}