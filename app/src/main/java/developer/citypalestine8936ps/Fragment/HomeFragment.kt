package developer.citypalestine8936ps.Fragment

import android.content.Intent
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
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import developer.citypalestine8936ps.PostDetailsActivity
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.adapters.CityAdapter
import developer.citypalestine8936ps.adapters.PostAdpter
import developer.citypalestine8936ps.databinding.FragmentHomeBinding
import developer.citypalestine8936ps.listeners.PostListener
import developer.citypalestine8936ps.models.City
import developer.citypalestine8936ps.models.Post
import developer.citypalestine8936ps.utilites.Constants
import developer.citypalestine8936ps.utilites.PreferenceManager


class HomeFragment : Fragment() , PostListener{
    private lateinit var postAdpter: PostAdpter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mMap: GoogleMap
    private lateinit var database: FirebaseFirestore
    var dataPost: ArrayList<Post> = ArrayList<Post>()
    lateinit var cityAdapter :CityAdapter
    lateinit var cityLocation: LatLng
    private var preferenceManager: PreferenceManager? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        database = FirebaseFirestore.getInstance()
        preferenceManager = PreferenceManager(activity)


        getAllPost(preferenceManager!!.getString(Constants.KEY_IMAGE) ,
            preferenceManager!!.getString(Constants.KEY_USER_ID))

//        for (city in data){
//            city.lat
//            cityLocation= LatLng(city.lat.toDouble(),city.lat.toDouble())
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(cityLocation))
//
//            mMap.addMarker(MarkerOptions().position(cityLocation).title(city.cityName))
//        }
        return binding.root;
    }

//    override fun onMapReady(p0: GoogleMap) {
//        mMap = p0
//
//
//        database.collection(Constants.KEY_COLLECTION_CITY).get()
//            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
//
//                data.clear()
//                for (documentSnapshot in queryDocumentSnapshots) {
//                    val city: City = documentSnapshot.toObject(City::class.java)
//                    city.id =documentSnapshot.id
//
//                    data.add(city)
//
//                    val sydney = LatLng(city.lat.toDouble(), city.lng.toDouble())
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//                    mMap.addMarker(MarkerOptions().position(sydney).title(city.cityName))
//                    mMap.moveCamera(CameraUpdateFactory.zoomTo(8f))
////                    cityAdapter = CityAdapter( data , activity)
////                    binding.cityRec.setAdapter(cityAdapter)
////                    Log.e("hin", data.size.toString() + "")
////                    binding.cityRec.setLayoutManager(LinearLayoutManager(activity))
//                    database.collection(Constants.KEY_COLLECTION_CITY).document(city.id).collection("Post").get().addOnSuccessListener { queryDocumentSnapshots ->
//
//                        for (documentSnapshot in queryDocumentSnapshots) {
//                            val post: Post = documentSnapshot.toObject(Post::class.java)
//                            Log.e("hin555", post.postDec.toString() + "")
//                            Log.e("hin55", documentSnapshot.id + "")
//
//                            dataPost.add(post)
//                        }
//                        postAdpter = PostAdpter(dataPost, activity  , preferenceManager!!.getString(Constants.KEY_IMAGE))
//                        binding.cityRec.setAdapter(postAdpter)
//
//                        //    binding.p.visibility = View.GONE
//                        binding.cityRec.visibility = View.VISIBLE
//
//                        Log.e("hin", data.size.toString() + "")
//                        binding.cityRec.setLayoutManager(LinearLayoutManager(activity))
//
//                    }
//
//                }
//            }).addOnFailureListener(OnFailureListener { e ->
//                Log.e("hind", e.message!!)
//            })
//
//
//    }

    private  fun getAllPost( userImage:String ,userId: String){
        var data = ArrayList<Post>()
        binding.progressbar.visibility = View.VISIBLE

        database.collection(Constants.KEY_COLLECTION_CITY).get().addOnSuccessListener{queryDocumentSnapshots ->
            for (documentSnapshot in queryDocumentSnapshots){
                documentSnapshot.id
                database.collection(Constants.KEY_COLLECTION_CITY).document(documentSnapshot.id).collection("Post")
                    .get().addOnSuccessListener { queryDocumentSnapshots1 ->
                    Log.e("hin5", "${queryDocumentSnapshots1.documents.size}  kdkkdkd")

                    for (documentSnapshot1 in queryDocumentSnapshots1) {
                        val post: Post = documentSnapshot1.toObject(Post::class.java)
                        Log.e("hin555", post.postDec.toString() + "")
                        Log.e("hin55", documentSnapshot1.id + "")

                        data.add(post)
                    }
                    postAdpter = PostAdpter(data, activity  , userImage ,userId ,this)
                    binding.cityRec.setAdapter(postAdpter)

                    //    binding.p.visibility = View.GONE
                    binding.cityRec.visibility = View.VISIBLE
                    binding.progressbar.visibility = View.GONE
                    binding.cityRec.visibility = View.VISIBLE


                    Log.e("hin", data.size.toString() + "")
                    binding.cityRec.setLayoutManager(LinearLayoutManager(activity))

                }


            }

        }
    }

    override fun onPostClicked(post: Post) {
        val intent = Intent(activity  , PostDetailsActivity::class.java)
        intent.putExtra("Post", post)
        intent.putExtra("comment", post.numberOfComment)
        //   preferenceManager!!.putString(Constants.KEY_USER , post!!.id)

        startActivity(intent)
    }

//    private fun getAllCity() {
//
//        database.collection(Constants.KEY_COLLECTION_CITY).get()
//            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
//                for (documentSnapshot in queryDocumentSnapshots) {
//                    val city: City = documentSnapshot.toObject(City::class.java)
//                    city.id =documentSnapshot.id
//
//                    data.add(city)
//
//                    cityAdapter = CityAdapter( data , activity)
//                    binding.cityRec.setAdapter(cityAdapter)
//                    Log.e("hin", data.size.toString() + "")
//                    binding.cityRec.setLayoutManager(LinearLayoutManager(activity))
//                }
//            }).addOnFailureListener(OnFailureListener { e ->
//            Log.e("hind", e.message!!)
//        })
//
//
//    }




}