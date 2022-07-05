package developer.citypalestine8936ps.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import developer.citypalestine8936ps.BookActivity
import developer.citypalestine8936ps.PalestinDetialsActivity
import developer.citypalestine8936ps.ProfileActivity
import developer.citypalestine8936ps.databinding.FragmentMoreBinding


class MoreFragment : Fragment() {

    lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(inflater, container, false)

        binding.txtPalestinTrsadotional.setOnClickListener(View.OnClickListener {

            var intent = Intent(activity, PalestinDetialsActivity::class.java)
            intent.putExtra("tradition", true)
            startActivity(intent)
        })
        binding.txtBook.setOnClickListener {
            startActivity(Intent(activity, BookActivity::class.java))
        }

        binding.txtProfile.setOnClickListener {
            startActivity(Intent(activity, ProfileActivity::class.java))
        }
        binding.txtPalestinVillages.setOnClickListener(View.OnClickListener {

            var intent = Intent(activity, PalestinDetialsActivity::class.java)
            intent.putExtra("Villages", true)
            startActivity(intent)
        })
        binding.txtAboutPalestine.setOnClickListener(View.OnClickListener {

            var intent = Intent(activity, PalestinDetialsActivity::class.java)
            intent.putExtra("about", true)
            startActivity(intent)
        })

        binding.txetAboutapp.setOnClickListener(View.OnClickListener {

            var intent = Intent(activity, PalestinDetialsActivity::class.java)
            intent.putExtra("aboutApp", true)
            startActivity(intent)
        })
        binding.txetRes.setOnClickListener(View.OnClickListener {

            var intent = Intent(activity, PalestinDetialsActivity::class.java)
            intent.putExtra("res", true)
            startActivity(intent)
        })
        return binding.root
    }


}