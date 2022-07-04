package developer.citypalestine8936ps.city_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import developer.citypalestine8936ps.adapters.FamilyAdapter
import developer.citypalestine8936ps.adapters.LandMarkAdapter
import developer.citypalestine8936ps.databinding.ActivityFamousFamiliesBinding
import developer.citypalestine8936ps.models.City
import developer.citypalestine8936ps.models.LandMark
import developer.citypalestine8936ps.utilites.Constants

class FamousFamiliesActivity : AppCompatActivity() {

    private var adapter: FamilyAdapter? = null
    private var landMarkAdapter: LandMarkAdapter? = null
    private lateinit var database: FirebaseFirestore
    lateinit var binding : ActivityFamousFamiliesBinding
    lateinit var  city: City
      var  shuadaa: Boolean = false
      var  family: Boolean = false
      var  landmark: Boolean = false
      var  tradition: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFamousFamiliesBinding.inflate(layoutInflater)
        database=FirebaseFirestore.getInstance()
        setContentView(binding.root)
        city  = intent.getSerializableExtra("cityObj") as City
        shuadaa  = intent.getBooleanExtra("shuadaa" , false)
        family  = intent.getBooleanExtra("family" , false)
        landmark  = intent.getBooleanExtra("landmark" , false)
        tradition  = intent.getBooleanExtra("tradition" , false)
        if (shuadaa !=null && shuadaa){
            getAllShuahaa()
            binding.txt.setText( "${city.cityName} شهداء")
        }else if(family !=null && family){
            binding.txt.setText( "${city.cityName} اشهر عائلات")

            getAllFamily()
        }else if (landmark !=null && landmark){
         getAllLandMark()
            binding.txt.setText( "${city.cityName}معالم مدينة ")

        }

        Log.e("hinddd" , city.id + "IIIIId")

    }


    private fun getAllShuahaa() {
        database.collection(Constants.KEY_COLLECTION_CITY).document(city.id).get()
            .addOnCompleteListener() { queryDocumentSnapshots ->


                queryDocumentSnapshots.result!!.get("families")


                var data = queryDocumentSnapshots.result.get("shuhadaa") as ArrayList<String>

                adapter = FamilyAdapter(applicationContext, data)
                binding.familyRc.setHasFixedSize(true)

                binding.familyRc.setLayoutManager(LinearLayoutManager(this))

                binding.familyRc.setAdapter(adapter)
                adapter!!.notifyDataSetChanged()
            }
    }

    private fun getAllFamily() {

        database.collection(Constants.KEY_COLLECTION_CITY).document(city.id).get()
            .addOnCompleteListener() { queryDocumentSnapshots ->


                queryDocumentSnapshots.result!!.get("families")


                var data = queryDocumentSnapshots.result.get("families") as ArrayList<String>

                adapter = FamilyAdapter(applicationContext, data)
                binding.familyRc.setHasFixedSize(true)

                binding.familyRc.setLayoutManager(LinearLayoutManager(this))

                binding.familyRc.setAdapter(adapter)
                adapter!!.notifyDataSetChanged()
            }
    }

    private fun getAllLandMark() {

        database.collection(Constants.KEY_COLLECTION_CITY).document(city.id).collection("Landmark").get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                var data: ArrayList<LandMark> = ArrayList<LandMark>()

                for (documentSnapshot in queryDocumentSnapshots) {
                val landMark: LandMark = documentSnapshot.toObject(LandMark::class.java)
               // landMark.id =documentSnapshot.id

                data.add(landMark)

            }
                landMarkAdapter = LandMarkAdapter( this ,data)
                binding.familyRc.adapter= (landMarkAdapter)
                Log.e("hin", data.size.toString() + "")
                binding.familyRc.setLayoutManager(LinearLayoutManager(this))
        }).addOnFailureListener(OnFailureListener { e ->
            Log.e("hind", e.message!!)
        })
    }

}