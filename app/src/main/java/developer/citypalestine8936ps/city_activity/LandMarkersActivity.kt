package developer.citypalestine8936ps.city_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.adapters.FamilyAdapter
import developer.citypalestine8936ps.databinding.ActivityLandMarkersBinding
import developer.citypalestine8936ps.models.City

class LandMarkersActivity : AppCompatActivity() {
    private var adapter: FamilyAdapter? = null
    private lateinit var database: FirebaseFirestore
    lateinit var binding : ActivityLandMarkersBinding
    lateinit var  city: City
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_land_markers)
        binding = ActivityLandMarkersBinding.inflate(layoutInflater)
        database=FirebaseFirestore.getInstance()
        city  = intent.getSerializableExtra("cityObj") as City
        setContentView(binding.root)

        //Landmark
    }

//    private fun getAllShuahaa() {
//        database.collection(Constants.KEY_COLLECTION_CITY).document(city.id).get()
//            .addOnCompleteListener() { queryDocumentSnapshots ->
//
//
//                queryDocumentSnapshots.result!!.get("families")
//
//
//                var data = queryDocumentSnapshots.result.get("shuhadaa") as ArrayList<String>
//
//                adapter = FamilyAdapter(applicationContext, data)
//                binding.familyRc.setHasFixedSize(true)
//
//                binding.familyRc.setLayoutManager(LinearLayoutManager(this))
//
//                binding.familyRc.setAdapter(adapter)
//                adapter!!.notifyDataSetChanged()
//            }
//    }
}