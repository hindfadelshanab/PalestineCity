package developer.citypalestine8936ps.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.citypalestine8936ps.databinding.ItemCityBinding
import developer.citypalestine8936ps.models.NewCity

class NewCityAdapter(
    private var cities: MutableList<NewCity>
) : RecyclerView.Adapter<NewCityAdapter.NewCityViewHolder>() {

    fun insertCity(city: NewCity) {
        cities.add(city)
        notifyItemInserted(cities.size - 1)
    }

    fun updateData(data: MutableList<NewCity>) {
        cities = data
        notifyDataSetChanged()
    }

    inner class NewCityViewHolder(private val binding: ItemCityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(city: NewCity) {
            binding.tvCityName.text = city.cityName
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