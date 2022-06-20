package developer.citypalestine8936ps

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import developer.citypalestine8936ps.adapters.PhotoAdapter
import developer.citypalestine8936ps.databinding.ActivityPhotoCityBinding
import developer.citypalestine8936ps.models.City
import developer.citypalestine8936ps.utilites.Constants

class PhotoCityActivity : AppCompatActivity() {


    private var adapter: PhotoAdapter? = null
    private lateinit var database: FirebaseFirestore
    lateinit var binding :ActivityPhotoCityBinding
    lateinit var  city: City
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoCityBinding.inflate(layoutInflater)
        database=FirebaseFirestore.getInstance()
        setContentView(binding.root)
       city  = intent.getSerializableExtra("cityObj") as City

        Log.e("hinddd" , city.id + "IIIIId")
        getSupportActionBar()?.setTitle("Gallery");

        getAllPhoto()

    }

    private fun getAllPhoto(){


        binding.progress.visibility =View.VISIBLE
        database.collection(Constants.KEY_COLLECTION_CITY).document(city.id).collection("Photo")
            .get().addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->

                 var doc = queryDocumentSnapshots.documents[0]

                var data  =doc.get("images")as ArrayList<String>

                adapter = PhotoAdapter(applicationContext, data)
                binding.recyclerViewPhoto.setHasFixedSize(true)
                val staggeredGridLayoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                binding.recyclerViewPhoto.setLayoutManager(staggeredGridLayoutManager)

                binding.recyclerViewPhoto.setAdapter(adapter)
                adapter!!.notifyDataSetChanged()
                binding.progress.setVisibility(View.INVISIBLE)


                binding.progress.visibility =View.GONE
//                for (documentSnapshot in queryDocumentSnapshots) {
//
//                    documentSnapshot.data.get("images")
//
//
//                }

            }       )

//            .addOnCompleteListener() { queryDocumentSnapshots ->
//
//             //   queryDocumentSnapshots.data!!.get("images")
//                queryDocumentSnapshots.result.id
//
//
//
//                 var data  =queryDocumentSnapshots.result.get("images")as ArrayList<String>
//
//                adapter = PhotoAdapter(applicationContext, data)
//                binding.recyclerViewPhoto.setHasFixedSize(true)
//                val staggeredGridLayoutManager =
//                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//                binding.recyclerViewPhoto.setLayoutManager(staggeredGridLayoutManager)
//
//                binding.recyclerViewPhoto.setAdapter(adapter)
//                adapter!!.notifyDataSetChanged()
//                binding.progress.setVisibility(View.INVISIBLE)
//
//                Log.e("phooot" ,  "phottt ${  queryDocumentSnapshots.result.id}")
//                Log.e("phooot" ,  "phottt ${ data.size}")
//
//
//
//            }
//            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
//                for (documentSnapshot in queryDocumentSnapshots) {
//
//
////                    val city: City = documentSnapshot.toObject(City::class.java)
////                    city.id =documentSnapshot.id
////
////                    list.add(city)
////
////                    cityAdapter = CityAdapter( data , activity)
////                    binding.cityRec.setAdapter(cityAdapter)
////                    Log.e("hin", data.size.toString() + "")
////                    binding.cityRec.setLayoutManager(LinearLayoutManager(activity))
//                }
            .addOnFailureListener(OnFailureListener { e ->

                binding.progress.visibility =View.VISIBLE
                Log.e("hind", e.message!!)
            })

    }
}