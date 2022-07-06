package developer.citypalestine8936ps.new_more_feature.app_resource

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import developer.citypalestine8936ps.databinding.ActivityAppResourcesBinding

class AppResourcesActivity : AppCompatActivity() {

    private val resourceAdapter by lazy {
        ResourcesAdapter(listOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAppResourcesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvResources.adapter = resourceAdapter

        resourceAdapter.updateData(
            listOf(
                ResourceData("موقع الحدث", "https://www.alhadath.ps"),
                ResourceData("مكتبة النور", "https://www.noor-book.com"),
                ResourceData("ويكبيديا", "https://ar.wikipedia.org"),
            )
        )
    }
}