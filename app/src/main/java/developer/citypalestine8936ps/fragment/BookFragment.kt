package developer.citypalestine8936ps.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import developer.citypalestine8936ps.adapters.BookTypeAdpter
import developer.citypalestine8936ps.adapters.CityAdapter
import developer.citypalestine8936ps.databinding.FragmentBookBinding
import developer.citypalestine8936ps.models.City
import developer.citypalestine8936ps.utilites.Constants


class BookFragment : Fragment() , OnMapReadyCallback {


    private lateinit var cityAdapter: CityAdapter
    lateinit var binding: FragmentBookBinding
    lateinit var database: FirebaseFirestore
    lateinit var adapter: BookTypeAdpter
    private lateinit var mMap: GoogleMap
    var data: ArrayList<City> = ArrayList<City>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookBinding.inflate(inflater, container, false)
        database = FirebaseFirestore.getInstance()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment

        //     getAllBook()
        getAllCity()
        mapFragment.getMapAsync(this)
        return binding.root
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
//                    cityAdapter = CityAdapter( data , activity)
//                    binding.cityRec.setAdapter(cityAdapter)
//                    Log.e("hin", data.size.toString() + "")
//                    binding.cityRec.setLayoutManager(LinearLayoutManager(activity))
//                    database.collection(Constants.KEY_COLLECTION_CITY).document(city.id).collection("Post").get().addOnSuccessListener { queryDocumentSnapshots ->

//                        for (documentSnapshot in queryDocumentSnapshots) {
//                            val post: Post = documentSnapshot.toObject(Post::class.java)
//                            Log.e("hin555", post.postDec.toString() + "")
//                            Log.e("hin55", documentSnapshot.id + "")
//
//                            dataPost.add(post)
//                        }
                        cityAdapter = CityAdapter(data, activity)

                        binding.citysRc.setAdapter(cityAdapter)
                        //    binding.p.visibility = View.GONE
                        binding.citysRc.visibility = View.VISIBLE

                        Log.e("hin", data.size.toString() + "")
                        binding.citysRc.setLayoutManager(LinearLayoutManager(activity))

                    }



            }).addOnFailureListener(OnFailureListener { e ->
                Log.e("hind", e.message!!)
            })


    }

    private fun getAllCity() {
        database.collection(Constants.KEY_COLLECTION_CITY).get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->

                var data: ArrayList<City> = ArrayList()
                data.clear()
                for (documentSnapshot in queryDocumentSnapshots) {
                    val city: City = documentSnapshot.toObject(City::class.java)
                    city.id = documentSnapshot.id

                    data.add(city)


                }
                cityAdapter = CityAdapter(data, activity)

                binding.citysRc.setAdapter(cityAdapter)
                //    binding.p.visibility = View.GONE
                binding.citysRc.visibility = View.VISIBLE

                Log.e("hin", data.size.toString() + "")
                binding.citysRc.setLayoutManager(LinearLayoutManager(activity))

            });

//    private fun getAllBook() {
//
//
//        database.collection("Book").get()
//            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
//                var data: ArrayList<BookType> = ArrayList<BookType>()
//                for (documentSnapshot in queryDocumentSnapshots) {
//                    val bookType: BookType = BookType()
//                    bookType.bookType = documentSnapshot.data.get("bookType")?.toString()
//
//                    bookType.books = ArrayList();
//
//                    database.collection("Book").document(documentSnapshot.id)
//                        .collection("AllBook").get().addOnSuccessListener { docs ->
//                            var dataBook: ArrayList<Book> = ArrayList<Book>()
//
//                            for (d in docs) {
//                                var book: Book = d.toObject(Book::class.java)
//                                Log.e("hin",book.bookName.toString() +"booo")
//                                Log.e("hin",book.bookPhoto.toString() +"booo")
//
//                                dataBook.add(book)
//                            }
//                            Log.e("hin", "${dataBook.size} datab book")
//                            bookType.books?.addAll(dataBook)
//                            data.add(bookType)
//                            Log.e("hin", "${bookType.books!!.size}sss")
//                            adapter = BookTypeAdpter(data)
//                            binding.bookRec.adapter = (adapter)
//                            Log.e("hin", data.size.toString() + "")
//                            binding.bookRec.setLayoutManager(LinearLayoutManager(activity))
//                        }
//                  //
//
//
//                }
//
//            }).addOnFailureListener(OnFailureListener { e ->
//                Log.e("hind", e.message!!)
//            })
//    }

    }
}