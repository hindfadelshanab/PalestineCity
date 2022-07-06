package developer.citypalestine8936ps.new_city_feature.landmarks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.citypalestine8936ps.databinding.ItemNewLandmarkBinding
import developer.citypalestine8936ps.new_city_feature.model.NewLandmarkData

class NewCityLandmarksAdapter(
    private val context: Context,
    private var landmarks: List<NewLandmarkData>,
    private var listener: LandmarkListener,
) : RecyclerView.Adapter<NewCityLandmarksAdapter.NewCityLandmarksViewHolder>() {

    fun updateData(data: List<NewLandmarkData>) {
        landmarks = data
        notifyDataSetChanged()
    }

    inner class NewCityLandmarksViewHolder(private val binding: ItemNewLandmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(landmark: NewLandmarkData) {
            binding.tvLandmarkName.text = landmark.name
            val landmarkPhotoAdapter = LandmarkPhotoAdapter(context, landmark.photos,listener)
            binding.rvLandmarkPhotos.adapter = landmarkPhotoAdapter

            binding.tvLandmarkShowMorePhotos.visibility = if (landmark.photos.size < 6) {
                View.GONE
            } else {
                View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewCityLandmarksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNewLandmarkBinding.inflate(inflater, parent, false)
        return NewCityLandmarksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewCityLandmarksViewHolder, position: Int) {
        val currentLandmark = landmarks[position]
        holder.bindItem(currentLandmark)
    }

    override fun getItemCount(): Int = landmarks.size
}