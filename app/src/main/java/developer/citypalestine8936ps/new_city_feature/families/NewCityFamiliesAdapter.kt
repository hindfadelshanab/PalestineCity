package developer.citypalestine8936ps.new_city_feature.families

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.citypalestine8936ps.databinding.ItemCityFamilyBinding

class NewCityFamiliesAdapter(
    private var families: List<String>
) : RecyclerView.Adapter<NewCityFamiliesAdapter.NewCityFamiliesViewHolder>() {

    fun updateData(data: List<String>){
        families = data
        notifyDataSetChanged()
    }

    class NewCityFamiliesViewHolder(private val binding: ItemCityFamilyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(family: String) {
            binding.tvFamilyName.text = family
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewCityFamiliesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCityFamilyBinding.inflate(inflater, parent, false)
        return NewCityFamiliesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewCityFamiliesViewHolder, position: Int) {
        val currentFamily = families[position]
        holder.bindItem(currentFamily)
    }

    override fun getItemCount(): Int = families.size
}