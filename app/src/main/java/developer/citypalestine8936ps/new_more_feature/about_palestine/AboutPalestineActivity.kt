package developer.citypalestine8936ps.new_more_feature.about_palestine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import developer.citypalestine8936ps.databinding.ActivityAboutPalestineBinding

class AboutPalestineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutPalestineBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutPalestineBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}