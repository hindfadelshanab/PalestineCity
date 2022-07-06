package developer.citypalestine8936ps.new_city_feature.martyrs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.citypalestine8936ps.databinding.ItemCityMartyrBinding

class NewCityMartyrsAdapter(
    private var martyrs: List<String>
) : RecyclerView.Adapter<NewCityMartyrsAdapter.NewCityMartyrsViewHolder>() {

    fun updateData(data: List<String>){
        martyrs = data
        notifyDataSetChanged()
    }

    class NewCityMartyrsViewHolder(private val binding: ItemCityMartyrBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(martyr: String) {
            binding.tvMartyrName.text = martyr
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewCityMartyrsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCityMartyrBinding.inflate(inflater, parent, false)
        return NewCityMartyrsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewCityMartyrsViewHolder, position: Int) {
        val currentMartyr = martyrs[position]
        holder.bindItem(currentMartyr)
    }

    override fun getItemCount(): Int = martyrs.size
}