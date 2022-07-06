package developer.citypalestine8936ps.new_city_feature

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.databinding.ActivityMapsBinding
import developer.citypalestine8936ps.new_city_feature.families.CityFamiliesActivity
import developer.citypalestine8936ps.new_city_feature.landmarks.NewCityLandmarkActivity
import developer.citypalestine8936ps.new_city_feature.martyrs.CityMartyrsActivity
import developer.citypalestine8936ps.new_city_feature.model.NewCityData
import developer.citypalestine8936ps.new_city_feature.photos.CityPhotoActivity
import developer.citypalestine8936ps.utilites.Constants

class CityProfileActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var database: FirebaseFirestore

    private lateinit var citiesCollectionRef: CollectionReference

    private lateinit var mMap: GoogleMap
    private var cityDocId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                initCityData()
            }
        }


        binding.apply {
            cvCityPhotos.setOnClickListener(this@CityProfileActivity)
            cvCityFamilies.setOnClickListener(this@CityProfileActivity)
            cvCityLandMarks.setOnClickListener(this@CityProfileActivity)
            cvCityMartyrs.setOnClickListener(this@CityProfileActivity)
            cvCityQuotes.setOnClickListener(this@CityProfileActivity)
        }


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapCity) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    private fun initCityData() {
//        DialogUtil(this).showLoadingDialog(true)
        citiesCollectionRef.document(cityDocId).get().addOnCompleteListener { it ->
//            DialogUtil(this).showLoadingDialog(false)
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
                supportActionBar?.title = city.name
                initCityMapMarker(city)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun initCityMapMarker(city: NewCityData) {
        val cityLocation = LatLng(city.lat, city.lng)
        mMap.addMarker(MarkerOptions().position(cityLocation).title(city.name))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cityLocation))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(8f))
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.cvCityPhotos -> onClickPhotos()
            binding.cvCityFamilies -> onClickFamilies()
            binding.cvCityLandMarks -> onClickLandMarks()
            binding.cvCityMartyrs -> onClickMartyrs()
            binding.cvCityQuotes -> onClickQuotes()
        }
    }

    private fun onClickPhotos() {
        val intent = Intent(this, CityPhotoActivity::class.java)
        intent.putExtras(bundleOf(Constants.KEY_CITY_DOC_ID to cityDocId))
        startActivity(intent)
    }

    private fun onClickFamilies() {
        val intent = Intent(this, CityFamiliesActivity::class.java)
        intent.putExtras(bundleOf(Constants.KEY_CITY_DOC_ID to cityDocId))
        startActivity(intent)
    }

    private fun onClickLandMarks() {
        val intent = Intent(this, NewCityLandmarkActivity::class.java)
        intent.putExtras(bundleOf(Constants.KEY_CITY_DOC_ID to cityDocId))
        startActivity(intent)
    }

    private fun onClickMartyrs() {
        val intent = Intent(this, CityMartyrsActivity::class.java)
        intent.putExtras(bundleOf(Constants.KEY_CITY_DOC_ID to cityDocId))
        startActivity(intent)
    }

    private fun onClickQuotes() {
        val intent = Intent(this, CityPhotoActivity::class.java)
        intent.putExtras(bundleOf(Constants.KEY_CITY_DOC_ID to cityDocId))
        startActivity(intent)
    }
}