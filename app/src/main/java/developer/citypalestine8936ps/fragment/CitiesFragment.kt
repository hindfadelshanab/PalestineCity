package developer.citypalestine8936ps.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import developer.citypalestine8936ps.adapters.NewCityAdapter
import developer.citypalestine8936ps.databinding.FragmentCitiesBinding
import developer.citypalestine8936ps.models.NewCity
import developer.citypalestine8936ps.utilites.Constants

class CitiesFragment : Fragment() {

    private lateinit var database: FirebaseFirestore
    private lateinit var binding: FragmentCitiesBinding
    private val newCityAdapter by lazy {
        NewCityAdapter(mutableListOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCitiesBinding.inflate(inflater);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCityList()
        loadCities()
    }

    private fun initCityList() {
        binding.rvCities.adapter = newCityAdapter
        binding.rvCities.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadCities() {
        database = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_COLLECTION_CITY).get()
            .addOnSuccessListener {
                val cities = it.toObjects(NewCity::class.java)
                Log.d(TAG, "loadCities: cities $cities")
                newCityAdapter.updateData(cities)
            }
    }

    companion object {
        private const val TAG = "CitiesFragment"
    }
}