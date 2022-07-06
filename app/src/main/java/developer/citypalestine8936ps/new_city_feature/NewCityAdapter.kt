package developer.citypalestine8936ps.new_city_feature

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.citypalestine8936ps.databinding.ItemCityBinding
import developer.citypalestine8936ps.new_city_feature.model.NewCityData
import developer.citypalestine8936ps.utilites.load

class NewCityAdapter(
    private val context: Context,
    private var cities: MutableList<NewCityData>,
    private var listener: CityListener
) : RecyclerView.Adapter<NewCityAdapter.NewCityViewHolder>() {

    fun insertCity(city: NewCityData) {
        cities.add(city)
        notifyItemInserted(cities.size - 1)
    }

    fun updateData(data: MutableList<NewCityData>) {
        cities = data
        notifyDataSetChanged()
    }

    inner class NewCityViewHolder(private val binding: ItemCityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(city: NewCityData) {
            binding.ivCityFeaturedImage.load(context, city.featuredImage)
            binding.tvCityName.text = city.name

            binding.root.setOnClickListener { listener.onClickCity(city) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewCityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCityBinding.inflate(inflater, parent, false)
        return NewCityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewCityViewHolder, position: Int) {
        val currentCity = cities[position]
        holder.bindItem(currentCity)
    }

    override fun getItemCount(): Int = cities.size
}