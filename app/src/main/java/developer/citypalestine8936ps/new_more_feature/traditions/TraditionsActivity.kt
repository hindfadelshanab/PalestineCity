package developer.citypalestine8936ps.new_more_feature.traditions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import developer.citypalestine8936ps.databinding.ActivityTraditionsBinding
import developer.citypalestine8936ps.new_image_preview.ImagePreviewDialog
import developer.citypalestine8936ps.utilites.Constants

class TraditionsActivity : AppCompatActivity(), TraditionalListener {
    private lateinit var binding: ActivityTraditionsBinding

    private lateinit var database: FirebaseFirestore
    private lateinit var villagesCollectionRef: CollectionReference

    private val traditionsAdapter by lazy {
        TraditionsAdapter(this, listOf(), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTraditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseFirestore.getInstance()
        villagesCollectionRef = database.collection(Constants.KEY_COLLECTION_TRADITION)
        initTraditionsList()
        initTraditionsData()
    }

    private fun initTraditionsList() {
        binding.rvTraditions.adapter = traditionsAdapter
    }

    private fun initTraditionsData() {
        villagesCollectionRef.get().addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }

            val traditions = it.result.toObjects(TraditionData::class.java)
            traditionsAdapter.updateData(traditions)
        }
    }

    override fun onClickTraditionPhoto(photoUrl: String) {
        ImagePreviewDialog.display(supportFragmentManager, photoUrl)
    }
}