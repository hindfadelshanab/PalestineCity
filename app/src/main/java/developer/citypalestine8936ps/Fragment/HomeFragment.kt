package developer.citypalestine8936ps.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.adapters.CityAdapter
import developer.citypalestine8936ps.databinding.FragmentHomeBinding
import developer.citypalestine8936ps.models.City
import developer.citypalestine8936ps.utilites.Constants


class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mMap: GoogleMap
    private lateinit var database: FirebaseFirestore
    var data: ArrayList<City> = ArrayList<City>()
    lateinit var cityAdapter :CityAdapter
    lateinit var cityLocation: LatLng

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        database = FirebaseFirestore.getInstance()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment

//        for (city in data){
//            city.lat
//            cityLocation= LatLng(city.lat.toDouble(),city.lat.toDouble())
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(cityLocation))
//
//            mMap.addMarker(MarkerOptions().position(cityLocation).title(city.cityName))
//        }
        mapFragment.getMapAsync(this)
        return binding.root;
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0


        database.collection(Constants.KEY_COLLECTION_CITY).get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->

                data.clear()
                for (documentSnapshot in queryDocumentSnapshots) {
                    val city: City = documentSnapshot.toObject(City::class.java)
                    city.id =documentSnapshot.id

                    data.add(city)

                    val sydney = LatLng(city.lat.toDouble(), city.lng.toDouble())
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                    mMap.addMarker(MarkerOptions().position(sydney).title(city.cityName))
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(8f))
                    cityAdapter = CityAdapter( data , activity)
                    binding.cityRec.setAdapter(cityAdapter)
                    Log.e("hin", data.size.toString() + "")
                    binding.cityRec.setLayoutManager(LinearLayoutManager(activity))
                }
            }).addOnFailureListener(OnFailureListener { e ->
                Log.e("hind", e.message!!)
            })


    }

    private fun getAllCity() {

        database.collection(Constants.KEY_COLLECTION_CITY).get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val city: City = documentSnapshot.toObject(City::class.java)
                    city.id =documentSnapshot.id

                    data.add(city)

                    cityAdapter = CityAdapter( data , activity)
                    binding.cityRec.setAdapter(cityAdapter)
                    Log.e("hin", data.size.toString() + "")
                    binding.cityRec.setLayoutManager(LinearLayoutManager(activity))
                }
            }).addOnFailureListener(OnFailureListener { e ->
            Log.e("hind", e.message!!)
        })


    }




}