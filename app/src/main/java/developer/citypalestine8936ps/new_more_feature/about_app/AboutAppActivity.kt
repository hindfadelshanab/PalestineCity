package developer.citypalestine8936ps.new_more_feature.about_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import developer.citypalestine8936ps.R

class AboutAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)
        Log.d("AboutAppActivity", "onCreate: ")
        supportActionBar?.title = getString(R.string.about)
    }
}