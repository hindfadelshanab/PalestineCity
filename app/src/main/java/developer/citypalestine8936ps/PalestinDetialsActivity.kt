package developer.citypalestine8936ps

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import developer.citypalestine8936ps.adapters.FamilyAdapter
import developer.citypalestine8936ps.adapters.LandMarkAdapter
import developer.citypalestine8936ps.databinding.ActivityPalestinDetialsBinding
import developer.citypalestine8936ps.models.LandMark


class PalestinDetialsActivity : AppCompatActivity() {

    private var landMarkAdapter: LandMarkAdapter? = null
    private var adapter: FamilyAdapter? = null

    private lateinit var database: FirebaseFirestore
    lateinit var binding : ActivityPalestinDetialsBinding
    var  tradition: Boolean = false
    var  villages: Boolean = false
    var  about: Boolean = false
    var  aboutApp: Boolean = false
    var  ress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPalestinDetialsBinding.inflate(layoutInflater)
        database = FirebaseFirestore.getInstance()
        setContentView(binding.root)
        tradition  = intent.getBooleanExtra("tradition" , false)
        villages  = intent.getBooleanExtra("Villages" , false)
        about  = intent.getBooleanExtra("about" , false)
        aboutApp  = intent.getBooleanExtra("aboutApp" , false)
        ress  = intent.getBooleanExtra("res" , false)
        getSupportActionBar()?.setTitle("Palestine");

        if (tradition !=null && tradition){
            getAllTradition()
            binding.txt.setText( "التراث")

        }else if(villages !=null && villages) {
            getAllVillages()
            binding.txt.setText("القرى الفلسطينية")
        }else if(about !=null && about) {

            binding.txt.setText("عن فلسطين")
            binding.traditionRc.visibility = View.GONE
            binding.layout.visibility = View.VISIBLE
        }else if(aboutApp !=null && aboutApp) {

            binding.txt.visibility =View.GONE
            binding.traditionRc.visibility = View.GONE
            binding.txtAboutapp.visibility = View.VISIBLE
            binding.imageLogo.visibility = View.VISIBLE
        }else if(ress !=null && ress) {

            binding.txt.setText("المصادر")
            binding.traditionRc.visibility = View.GONE
            binding.txtResoursec.visibility = View.VISIBLE
            binding.txtResoursec2.visibility = View.VISIBLE
            binding.txtResoursec3.visibility = View.VISIBLE
            binding.txtResoursec.setOnClickListener(View.OnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.noor-book.com"))
                startActivity(browserIntent)
            })
            binding.txtResoursec2.setOnClickListener(View.OnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.alhadath.ps"))
                startActivity(browserIntent)
            })
            binding.txtResoursec3.setOnClickListener(View.OnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ar.wikipedia.org"))
                startActivity(browserIntent)
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAllVillages() {
        database.collection("Villages").get()
            .addOnSuccessListener() { queryDocumentSnapshots ->
                var data: ArrayList<String> = ArrayList<String>()

                for (documentSnapshot in queryDocumentSnapshots) {

                    data.add(documentSnapshot.data.get("name").toString())

                }
                adapter = FamilyAdapter(applicationContext, data)
                binding.traditionRc.setHasFixedSize(true)

                binding.traditionRc.setLayoutManager(LinearLayoutManager(this))

                binding.traditionRc.setAdapter(adapter)
                adapter!!.notifyDataSetChanged()




            }
    }

    private fun getAllTradition() {

        database.collection("Tradition").get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                var data: ArrayList<LandMark> = ArrayList<LandMark>()

                for (documentSnapshot in queryDocumentSnapshots) {
                    val landMark: LandMark = documentSnapshot.toObject(LandMark::class.java)
                    // landMark.id =documentSnapshot.id
                    data.add(landMark)

                }
                landMarkAdapter = LandMarkAdapter( this ,data)
                binding.traditionRc.adapter= (landMarkAdapter)
                Log.e("hin", data.size.toString() + "")
                binding.traditionRc.setLayoutManager(LinearLayoutManager(this))
            }).addOnFailureListener(OnFailureListener { e ->
                Log.e("hind", e.message!!)
            })
    }

}