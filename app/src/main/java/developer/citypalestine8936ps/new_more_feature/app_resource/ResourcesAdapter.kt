package developer.citypalestine8936ps.new_more_feature.app_resource

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.citypalestine8936ps.databinding.ItemResourceBinding

class ResourcesAdapter(
    var resources: List<ResourceData>
) : RecyclerView.Adapter<ResourcesAdapter.ResourcesViewHolder>() {
    fun updateData(data: List<ResourceData>) {
        resources = data
        notifyDataSetChanged()
    }

    class ResourcesViewHolder(private val binding: ItemResourceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(resourceData: ResourceData) {
            binding.tvName.text = resourceData.name
            binding.tvLink.text = resourceData.link
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourcesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemResourceBinding.inflate(inflater, parent, false)
        return ResourcesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResourcesViewHolder, position: Int) {
        val currentResource = resources[position]
        holder.bindItem(currentResource)
    }

    override fun getItemCount(): Int  = resources.size
}