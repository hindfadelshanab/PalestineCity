package developer.citypalestine8936ps.new_more_feature.villages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import developer.citypalestine8936ps.databinding.ActivityVillagesBinding
import developer.citypalestine8936ps.utilites.Constants

class VillagesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVillagesBinding

    private lateinit var database: FirebaseFirestore
    private lateinit var villagesCollectionRef: CollectionReference

    private val villageAdapter by lazy {
        VillagesAdapter(listOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVillagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseFirestore.getInstance()
        villagesCollectionRef = database.collection(Constants.KEY_COLLECTION_VILLAGE)
        initVillagesList()
        initVillagesData()
    }

    private fun initVillagesList() {
        binding.rvVillages.adapter = villageAdapter
    }

    private fun initVillagesData() {
        villagesCollectionRef.get().addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }

            val villages = it.result.toObjects(VillageData::class.java)
            villageAdapter.updateData(villages)
        }
    }
}