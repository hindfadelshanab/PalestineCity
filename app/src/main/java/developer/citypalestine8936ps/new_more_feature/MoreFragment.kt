package developer.citypalestine8936ps.new_more_feature

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import developer.citypalestine8936ps.databinding.FragmentMoreBinding
import developer.citypalestine8936ps.new_more_feature.about_app.AboutAppActivity
import developer.citypalestine8936ps.new_more_feature.about_palestine.AboutPalestineActivity
import developer.citypalestine8936ps.new_more_feature.app_resource.AppResourcesActivity
import developer.citypalestine8936ps.new_more_feature.books.BooksActivity
import developer.citypalestine8936ps.new_more_feature.profile.ProfileActivity
import developer.citypalestine8936ps.new_more_feature.traditions.TraditionsActivity
import developer.citypalestine8936ps.new_more_feature.villages.VillagesActivity


class MoreFragment : Fragment() {

    lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(inflater, container, false)

        binding.txtProfile.setOnClickListener {
            startActivity(Intent(activity, ProfileActivity::class.java))
        }
        binding.txtPalestinTrsadotional.setOnClickListener {
            startActivity(Intent(activity, TraditionsActivity::class.java))
        }
        binding.txtPalestinVillages.setOnClickListener {
            startActivity(Intent(activity, VillagesActivity::class.java))
        }
        binding.txtAboutPalestine.setOnClickListener {
            startActivity(Intent(activity, AboutPalestineActivity::class.java))
        }
        binding.txtBook.setOnClickListener {
            startActivity(Intent(activity, BooksActivity::class.java))
        }
        binding.txetRes.setOnClickListener {
            startActivity(Intent(activity, AppResourcesActivity::class.java))
        }
        binding.txetAboutapp.setOnClickListener {
            startActivity(Intent(activity, AboutAppActivity::class.java))
        }
        return binding.root
    }


}