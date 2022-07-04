package developer.citypalestine8936ps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import developer.citypalestine8936ps.CityActivity.FamousFamiliesActivity
import developer.citypalestine8936ps.CityActivity.PopularProverbsActivity
import developer.citypalestine8936ps.CityActivity.PostActivity
import developer.citypalestine8936ps.databinding.ActivityMapsBinding
import developer.citypalestine8936ps.models.City

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var  city:City = intent.getSerializableExtra("city") as City

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setTitle(city.cityName)
        binding.btnCityPhoto.setOnClickListener(View.OnClickListener {


            var intent = Intent(this , PhotoCityActivity::class.java)
            intent.putExtra("cityObj" , city)
            startActivity(intent)


        })
        binding.btnCityFamily.setOnClickListener(View.OnClickListener {
         //   var  city:City = intent.getSerializableExtra("city") as City
            var intent = Intent(this , FamousFamiliesActivity::class.java)
            intent.putExtra("cityObj" , city)
            intent.putExtra("family" , true)

            startActivity(intent)


        })
        binding.btnCityShuhadaa.setOnClickListener(View.OnClickListener {
            //   var  city:City = intent.getSerializableExtra("city") as City
            var intent = Intent(this , FamousFamiliesActivity::class.java)
            intent.putExtra("cityObj" , city)
            intent.putExtra("shuadaa" , true)
            startActivity(intent)


        })
        binding.btnCityLandmark.setOnClickListener(View.OnClickListener {
            //   var  city:City = intent.getSerializableExtra("city") as City
            var intent = Intent(this , FamousFamiliesActivity::class.java)
            intent.putExtra("cityObj" , city)
            intent.putExtra("landmark" , true)
            startActivity(intent)

        })
        binding.btnCityPost.setOnClickListener(View.OnClickListener {
            //   var  city:City = intent.getSerializableExtra("city") as City
            var intent = Intent(this , PostActivity::class.java)
            intent.putExtra("cityObj" , city)
            startActivity(intent)

        })
        binding.btnCityShabea.setOnClickListener(View.OnClickListener {
            //   var  city:City = intent.getSerializableExtra("city") as City
            var intent = Intent(this , PopularProverbsActivity::class.java)
            startActivity(intent)


        })
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        var  city:City = intent.getSerializableExtra("city") as City
        if (city !=null){
            val sydney = LatLng(city.lat, city.lng)
            mMap.addMarker(MarkerOptions().position(sydney).title(city.cityName))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            mMap.moveCamera(CameraUpdateFactory.zoomTo(8f))
        }

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(31.522561, 34.453593)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Gaza"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}