package developer.citypalestine8936ps.new_city_feature.quotes

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.databinding.ActivityQuotesBinding
import developer.citypalestine8936ps.new_city_feature.model.NewCityData
import developer.citypalestine8936ps.utilites.Constants


class QuotesActivity : AppCompatActivity(R.layout.activity_quotes), QuoteListener {

    private val binding: ActivityQuotesBinding by viewBinding()
    private lateinit var database: FirebaseFirestore
    private lateinit var citiesCollectionRef: CollectionReference

    private var cityDocId: String = ""

    private val quotesAdapter by lazy {
        NewCityQuotesAdapter(listOf(), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                getCityData()
            }
        }
    }


    private fun getCityData() {
        citiesCollectionRef.document(cityDocId).get().addOnCompleteListener { it ->
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
                initCityQuotes(city)
            }
        }
    }

    private fun initCityQuotes(city: NewCityData) {
        binding.rvQuotes.adapter = quotesAdapter

        val list = city.quotes.ifEmpty {
            listOf(
                getString(R.string.somthing_wrong),
                getString(R.string.to_reset_password_enter_your_email_press_the_button_and_check_mail_to_follow_instruction),
                getString(R.string.small1_lorem),
                getString(R.string.small2_lorem),
                getString(R.string.small3_lorem),
                getString(R.string.somthing_wrong),
            )
        }
        quotesAdapter.updateData(list)
    }

    override fun onClickCopy(quote: String) {
        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("quote", quote)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, getString(R.string.copied_to_clip), Toast.LENGTH_SHORT).show()
    }

    override fun onClickShare(quote: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, quote)
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "Share To:"))
    }

    override fun onClickShareWhatsapp(quote: String) {
        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        whatsappIntent.setPackage("com.whatsapp")
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, quote)
        try {
            startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.no_whatsapp), Toast.LENGTH_SHORT).show()
        }
    }
}