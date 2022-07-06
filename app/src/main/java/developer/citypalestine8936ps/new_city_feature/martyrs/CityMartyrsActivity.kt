package developer.citypalestine8936ps.new_city_feature.martyrs

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.databinding.ActivityCityMartyrsBinding
import developer.citypalestine8936ps.new_city_feature.model.NewCityData
import developer.citypalestine8936ps.utilites.Constants

class CityMartyrsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCityMartyrsBinding

    private lateinit var database: FirebaseFirestore
    private lateinit var citiesCollectionRef: CollectionReference

    private var cityDocId: String = ""

    private val cityMartyrsAdapter by lazy {
        NewCityMartyrsAdapter(listOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityMartyrsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.gallery)

        database = FirebaseFirestore.getInstance()
        citiesCollectionRef = database.collection(Constants.KEY_COLLECTION_CITY)

        val receivedIntentExtra = intent.extras
        receivedIntentExtra?.let {
            if (it.containsKey(Constants.KEY_CITY_DOC_ID)) {
                cityDocId = it.getString(Constants.KEY_CITY_DOC_ID, "")
                if (cityDocId.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.somthing_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                getAllMartyrs()
            }
        }
    }

    private fun getAllMartyrs() {
        citiesCollectionRef.document(cityDocId).get().addOnCompleteListener { it ->
            if (!it.isSuccessful) {
                Toast.makeText(
                    this,
                    getString(R.string.somthing_wrong),
                    Toast.LENGTH_SHORT
                ).show()
                return@addOnCompleteListener
            }
            val city: NewCityData? = it.result.toObject(NewCityData::class.java)
            city?.let {
                initMartyrs(city)
            }
        }
    }


    private fun initMartyrs(city: NewCityData) {
        binding.rvCityMartyrs.adapter = cityMartyrsAdapter
        cityMartyrsAdapter.updateData(city.martyrs)
    }


}