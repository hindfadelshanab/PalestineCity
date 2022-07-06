package developer.citypalestine8936ps.new_city_feature.landmarks

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.databinding.ActivityNewCityLandmarkBinding
import developer.citypalestine8936ps.new_city_feature.model.NewLandmarkData
import developer.citypalestine8936ps.new_image_preview.ImagePreviewDialog
import developer.citypalestine8936ps.utilites.Constants

class NewCityLandmarkActivity : AppCompatActivity(), LandmarkListener {
    lateinit var binding: ActivityNewCityLandmarkBinding

    private lateinit var database: FirebaseFirestore
    private lateinit var citiesCollectionRef: CollectionReference
    private lateinit var landMarkCollectionRef: CollectionReference

    private var cityDocId: String = ""

    private val landmarkAdapter by lazy {
        NewCityLandmarksAdapter(this, listOf(),this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewCityLandmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.landmarks)

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
                landMarkCollectionRef = citiesCollectionRef.document(cityDocId)
                    .collection(Constants.KEY_COLLECTION_CITY_LANDMARK)
                getAllLandmarks()
            }
        }
    }

    private fun getAllLandmarks() {
        landMarkCollectionRef.get().addOnCompleteListener {
            if (!it.isSuccessful) {
                Toast.makeText(
                    this,
                    getString(R.string.somthing_wrong),
                    Toast.LENGTH_SHORT
                ).show()
                return@addOnCompleteListener
            }
            val landmarks = it.result.toObjects(NewLandmarkData::class.java)
            initLandmarks(landmarks)
        }
    }

    private fun initLandmarks(landmarks: List<NewLandmarkData>) {
        binding.rvCityLandmarks.adapter = landmarkAdapter
        landmarkAdapter.updateData(landmarks)
    }

    override fun onClickLandMarkPhoto(photoUrl: String) {
        ImagePreviewDialog.display(supportFragmentManager, photoUrl)
    }
}