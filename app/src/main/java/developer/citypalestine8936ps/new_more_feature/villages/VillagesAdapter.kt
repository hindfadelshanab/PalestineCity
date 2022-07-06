package developer.citypalestine8936ps.new_more_feature.villages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.citypalestine8936ps.databinding.ItemVillageBinding

class VillagesAdapter(
    private var villages: List<VillageData>
) : RecyclerView.Adapter<VillagesAdapter.VillagesViewHolder>() {

    fun updateData(data: List<VillageData>){
        villages = data
        notifyDataSetChanged()
    }

    class VillagesViewHolder(private val binding: ItemVillageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(villageData: VillageData) {
            binding.tvVillageName.text = villageData.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VillagesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVillageBinding.inflate(inflater, parent, false)
        return VillagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VillagesViewHolder, position: Int) {
        val currentVillageData = villages[position]
        holder.bindItem(currentVillageData)
    }

    override fun getItemCount(): Int = villages.size
}